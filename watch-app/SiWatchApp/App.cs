using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Text;
using System.Threading;
using SiWatchApp.Buffer;
using SiWatchApp.Configuration;
using SiWatchApp.Events;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Services;
using Xamarin.Forms;
using Notification = SiWatchApp.UI.Notification;

namespace SiWatchApp
{
    public class App : Application
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(App));

        private readonly MainPage _mainPage;

        private readonly SynchronizationContextScheduler _synchronizationContextScheduler;
        private readonly List<IDisposable> _disposables = new List<IDisposable>();

        private SettingsService _settingsService;
        private ProfileService _profileService;
        private Profile _profile;
        private Settings _settings;
        private PermissionManager _permissionManager;
        private InMemoryBuffer<Record> _buffer;
        private MonitoringPolicyService _monitoringPolicyService;
        private MonitoringService _monitoringService;
        private SyncService _syncService;
        private SOSEventSource _sosEventSource;
        private ActionEventSource _actionEventSource;
        private OutgoingEventHandler _outgoingEventHandler;
        private IncomingEventHandler _incomingEventHandler;

        public App()
        {
            _mainPage = new MainPage();
            _mainPage.SOSRequest += OnSOSRequest;
            _mainPage.ExitRequest += OnExitRequest;
            _mainPage.StartFinishRequest += OnStartFinishRequest;

            _synchronizationContextScheduler = new SynchronizationContextScheduler(SynchronizationContext.Current);
            Notification.Init();

            MainPage = _mainPage;
        }

        private bool _started = false;
        private void OnStartFinishRequest(object sender, EventArgs e)
        {
            if (_started) {
                _actionEventSource?.Signal("STOP", EventPriority.Urgent);
                _started = false;
                _mainPage.SetStartFinishText("START");
            }
            else {
                _actionEventSource?.Signal("START", EventPriority.Urgent);
                _started = true;
                _mainPage.SetStartFinishText("STOP");
            }
        }

        private async void OnExitRequest(object sender, EventArgs e)
        {
            var result = await Notification.ShowQuestion("Exit SiWatch?");
            if (true != result)
                return;

            LOGGER.Info("Exiting...");

            _monitoringService?.Dispose();
            _syncService?.Stop();
            _disposables?.ForEach(d => d.Dispose());
            LocationService.Instance.Dispose();
            
            Tizen.Applications.Application.Current.Exit();
        }

        private void OnSOSRequest(object sender, EventArgs e)
        {
            _sosEventSource?.Signal("SOS!");
        }

        protected override async void OnStart()
        {
            LOGGER.Info("OnStart");
            
            _settingsService = new SettingsService();
            _permissionManager = new PermissionManager();
            _buffer = new InMemoryBuffer<Record>();
            _disposables.Add(_buffer);

            _mainPage.SetStatus("Loading settings...");
            _settings = await _settingsService.GetSettings();
            _mainPage.SetDeviceId("DeviceID: " + _settings.DeviceId);
            _mainPage.SetApiUrl("API: " + _settings.ApiUrl);

            _mainPage.SetStatus("Checking permissions...");
            try {
                await _permissionManager.Demand("http://tizen.org/privilege/internet");
                await LocationService.Instance.DemandPermission(_permissionManager);
            }
            catch (Exception ex) {
                await Notification.ShowInfo("Error", ex.Message);
                Tizen.Applications.Application.Current.Exit();
                return;
            }

            try {
                await FeedbackService.Instance.DemandPermission(_permissionManager);
            }
            catch (Exception) {
                LOGGER.Info("Vibrator will be not available");
            }

            _mainPage.SetStatus("Loading profile...");
            _profileService = new ProfileService(_settings);
            try {
                _profile = await _profileService.GetProfile();
            }
            catch (Exception ex) {
                await Notification.ShowInfo("Error", ex.Message);
                Tizen.Applications.Application.Current.Exit();
                return;
            }

            _mainPage.SetStatus("Checking profile...");
            try {
                await _profileService.CheckProfile(_profile, _permissionManager);
            }
            catch (Exception ex) {
                await Notification.ShowInfo("Error", ex.Message);
                Tizen.Applications.Application.Current.Exit();
                return;
            }

            _mainPage.SetStatus("Initializing services...");
            if (LocationService.Instance.IsSupported) {
                try {
                    LocationService.Instance.Start();
                }
                catch (Exception ex) {
                    await Notification.ShowInfo("Error", "Location service error: "+ex.Message);
                    Tizen.Applications.Application.Current.Exit();
                    return;
                }
                var sub0 = Observable.Interval(TimeSpan.FromSeconds(2), _synchronizationContextScheduler)
                                     .Subscribe(_ => {
                                                    var location = LocationService.Instance.GetCurrentLocation();
                                                    var info = location == null ? "unknown" : "available";
                                                    _mainPage.SetLocationInfo($"Location {info}");
                                                });
                _disposables.Add(sub0);
            }
            else {
                await Notification.ShowInfo("Error", "Location service is not supported");
                Tizen.Applications.Application.Current.Exit();
                return;
            }

            _monitoringPolicyService = new MonitoringPolicyService(_profile);
            _monitoringService = new MonitoringService(_monitoringPolicyService, _buffer);
            _disposables.Add(_monitoringService);

            _incomingEventHandler = new IncomingEventHandler();

            _syncService = new SyncService(_monitoringPolicyService, _buffer, _settings,
                                           _incomingEventHandler.NotifyOn(_synchronizationContextScheduler));
            _syncService.Synced += OnSynced;

            _outgoingEventHandler = new OutgoingEventHandler(_buffer, _syncService);

            _sosEventSource = new SOSEventSource(LocationService.Instance);
            var sub1 = _sosEventSource.ObserveOn(TaskPoolScheduler.Default).Subscribe(_outgoingEventHandler);
            _disposables.Add(sub1);

            _actionEventSource = new ActionEventSource(LocationService.Instance);
            var sub2 = _actionEventSource.ObserveOn(TaskPoolScheduler.Default).Subscribe(_outgoingEventHandler);
            _disposables.Add(sub2);
            
            _monitoringService.Start();
            _syncService.Start();

            _mainPage.SetStatus("Working...");

            _mainPage.EnableSOS(true);
            _mainPage.SetStartFinishText("START");
            _mainPage.EnableStartFinish(true);

            var sub3 = Observable.Interval(TimeSpan.FromSeconds(2), _synchronizationContextScheduler)
                                 .Subscribe(_ => {
                                                _mainPage.SetStatus("Monitoring...");
                                                _mainPage.SetBufferInfo($"Buffered {_buffer.Count} records");
                                            });
            _disposables.Add(sub3);
        }

        private void OnSynced(object sender, EventArgs e)
        {
            _mainPage.SetStatus("Synchronized");
        }

        protected override void OnSleep()
        {
            LOGGER.Info("OnSleep");
        }

        protected override void OnResume()
        {
            LOGGER.Info("OnResume");
        }
    }
}
