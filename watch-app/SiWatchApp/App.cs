using System;
using System.Collections.Generic;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Threading;
using SiWatchApp.Buffer;
using SiWatchApp.Configuration;
using SiWatchApp.Events;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Queue;
using SiWatchApp.Services;
using SiWatchApp.System;
using Xamarin.Forms;
using Application = Xamarin.Forms.Application;
using Notification = SiWatchApp.UI.Notification;

namespace SiWatchApp
{
    public class App : Application
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(App));

        private readonly MainPage _mainPage;

        private SynchronizationContextScheduler _uiScheduler;
        private readonly List<IDisposable> _disposables = new List<IDisposable>();

        private SettingsService _settingsService;
        private ProfileService _profileService;
        private Profile _profile;
        private Settings _settings;
        private PermissionManager _permissionManager;
        private InMemoryBuffer<Record> _buffer;
        private InMemoryPriorityQueue<TextMessage> _messageQueue;
        private MonitoringPolicyService _monitoringPolicyService;
        private MonitoringService _monitoringService;
        private SyncService _syncService;
        private SOSEventSource _sosEventSource;
        private ActionEventSource _actionEventSource;
        private OutgoingEventHandler _outgoingEventHandler;
        private IncomingEventHandler _incomingEventHandler;
        private IJsonStorage _storage;
        private BufferingSyncProxy _syncProxy;

        public App()
        {
            _mainPage = new MainPage();
            _mainPage.SOSRequest += OnSOSRequest;
            _mainPage.ExitRequest += OnExitRequest;
            _mainPage.StartFinishRequest += OnStartFinishRequest;
            _mainPage.ReadMessageRequest += OnReadMessageRequest;
            
            MainPage = _mainPage;
        }

        private void InvokeOnMainThread(Action action)
        {
            if (Device.IsInvokeRequired) {
                Device.BeginInvokeOnMainThread(action);
            }
            else {
                action();
            }
        }

        private async void OnReadMessageRequest(object sender, EventArgs e)
        {
            if(_messageQueue == null)
                return;

            TextMessage message = await _messageQueue.Get();
            if (message != null) {
                await Notification.ShowInfo("Incoming message", message.Text);
            }
        }

        private bool _started = false;
        private void OnStartFinishRequest(object sender, EventArgs e)
        {
            if (_started) {
                DevicePower.ReleaseLock(PowerLock.DisplayNormal);
                _actionEventSource?.Signal("RouteFinish", EventPriority.Urgent);
                SetStatus("RouteFinish signaled!");
                _started = false;
                _mainPage.SetStartFinishText("START");
                _monitoringService?.Stop();
            }
            else {
                DevicePower.RequestLock(PowerLock.DisplayNormal, TimeSpan.Zero);
                _actionEventSource?.Signal("RouteStart", EventPriority.Urgent);
                SetStatus("RouteStart signaled!");
                _started = true;
                _mainPage.SetStartFinishText("FINISH");
                _monitoringService?.Start();
            }
        }

        private void Shutdown()
        {
            SetStatus("Shutting down...");
            LOGGER.Info("Shutting down...");
            if (_monitoringPolicyService != null) {
                _monitoringPolicyService.MonitoringPolicyChanged -= OnMonitoringPolicyChanged;
            }
            _monitoringService?.Dispose();
            _syncService?.Dispose();
            _disposables?.ForEach(d => d.Dispose());
            LocationService.Instance.Dispose();

            Tizen.Applications.Application.Current.Exit();
        }

        private void OnExitRequest(object sender, EventArgs e)
        {
            Notification.ShowQuestion("Exit SiWatch?", r => { if (true == r) Shutdown(); });
        }

        private void OnSOSRequest(object sender, EventArgs e)
        {
            _sosEventSource?.Signal("SOS!");
            _mainPage.SetStatus("SOS signaled!");
        }

        protected override void OnStart()
        {
            LOGGER.Info("OnStart");
        }

        private void SetStatus(string s)
        {
            InvokeOnMainThread(() => _mainPage.SetStatus(s));
        }

        public async void Init()
        {
            LOGGER.Info("Init");
            
            _uiScheduler = new SynchronizationContextScheduler(SynchronizationContext.Current);
            Notification.Init();

            _settingsService = new SettingsService();
            _permissionManager = new PermissionManager();
            _buffer = new InMemoryBuffer<Record>();
            _disposables.Add(_buffer);
            _messageQueue = new InMemoryPriorityQueue<TextMessage>();
            _disposables.Add(_messageQueue);

            SetStatus("Getting settings...");
            _settings = await _settingsService.GetSettings();
            _mainPage.SetPolicyInfo("DeviceID: " + _settings.DeviceId);
            _mainPage.SetApiUrl(_settings.ApiUrl);

            // check mandatory privileges
            SetStatus("Demand permissions...");
            try {
                await _permissionManager.Demand("http://tizen.org/privilege/internet", DevicePower.Privilege, LocationService.Privilege);
            }
            catch (Exception ex) {
                await Notification.ShowInfo("Error", "Permissions checking error: "+ex.Message);
                Tizen.Applications.Application.Current.Exit();
                return;
            }

            // check optional privileges
            try
            {
                await _permissionManager.Demand(FeedbackService.Privilege);
            }
            catch (Exception) {
                LOGGER.Info("Vibrator will be not available");
            }

            SetStatus("Loading profile...");
            _profileService = new ProfileService(_settings);
            try {
                _profile = await _profileService.GetProfile();
            }
            catch (Exception ex) {
                await Notification.ShowInfo("Error", "Failed loading profile: "+ex.Message);
                Tizen.Applications.Application.Current.Exit();
                return;
            }

            SetStatus("Checking profile...");
            try {
                await _profileService.CheckProfile(_profile, _permissionManager);
            }
            catch (Exception ex) {
                await Notification.ShowInfo("Error", "Failed checking profile: " + ex.Message);
                Tizen.Applications.Application.Current.Exit();
                return;
            }

            SetStatus("Initializing services...");
            if (LocationService.Instance.IsSupported) {
                try {
                    LocationService.Instance.Start();
                }
                catch (Exception ex) {
                    await Notification.ShowInfo("Error", "Location service error: "+ex.Message);
                    Tizen.Applications.Application.Current.Exit();
                    return;
                }
            }
            else {
                await Notification.ShowInfo("Error", "Location service is not supported");
                Tizen.Applications.Application.Current.Exit();
                return;
            }

            _monitoringPolicyService = new MonitoringPolicyService(_profile);
            _monitoringPolicyService.MonitoringPolicyChanged += OnMonitoringPolicyChanged;
            UpdateMonitoringPolicy();

            _monitoringService = new MonitoringService(_monitoringPolicyService, _buffer);
            _incomingEventHandler = new IncomingEventHandler(_messageQueue);

            _storage = new StringJsonStorage();
            _disposables.Add(_storage);

            _syncProxy = new BufferingSyncProxy(_storage, _settings);
            _syncProxy.Synced += OnProxySynced;
            
            _syncService = new SyncService(_monitoringPolicyService, _buffer, _syncProxy, _settings, _incomingEventHandler);
            _syncService.Synced += OnSynced;
            
            _outgoingEventHandler = new OutgoingEventHandler(_buffer, _syncService);

            _sosEventSource = new SOSEventSource(LocationService.Instance);
            var sub1 = _sosEventSource.ObserveOn(TaskPoolScheduler.Default).Subscribe(_outgoingEventHandler);
            _disposables.Add(sub1);

            _actionEventSource = new ActionEventSource(LocationService.Instance);
            var sub2 = _actionEventSource.ObserveOn(TaskPoolScheduler.Default).Subscribe(_outgoingEventHandler);
            _disposables.Add(sub2);
            
            //_monitoringService.Start();
            _syncService.Start();

            SetStatus("Ready");

            _mainPage.EnableSOS(true);
            _mainPage.SetStartFinishText("START");
            _mainPage.EnableStartFinish(true);

            // ui info update job
            var sub3 = Observable.Interval(TimeSpan.FromSeconds(2), _uiScheduler)
                                 .Subscribe(_ => {
                                                _mainPage.SetStatus(_started ? "Monitoring..." : "Waiting...");
                                                _mainPage.SetBufferInfo(_buffer.Count > 0 ? $"Queued {_buffer.Count} records" : "Queue is EMPTY");
                                                _mainPage.SetTime(DateTime.Now.ToString("HH:mm"));

                                                if (_messageQueue != null) _mainPage.ShowMessageButton(_messageQueue.Count > 0);

                                                var location = LocationService.Instance.GetCurrentLocation();
                                                if (location == null) {
                                                    _mainPage.SetLocationInfo("Location is unavailable", false);
                                                }
                                                else {
                                                    var info = $"Lat={location.Latitude:F5}, Lon={location.Longitude:F5}";
                                                    _mainPage.SetLocationInfo(info, location.Accuracy < 20);
                                                }
                                            });
            _disposables.Add(sub3);
        }
        
        private void UpdateMonitoringPolicy()
        {
            var name = _monitoringPolicyService.CurrentMonitoringPolicyName;
            _mainPage.SetPolicyInfo("Power profile: "+ (name == null ? "OFF" : name+"%"));
        }

        private void OnMonitoringPolicyChanged(object sender, MonitoringPolicy e)
        {
            InvokeOnMainThread(UpdateMonitoringPolicy);
        }

        private void OnProxySynced(object sender, SyncResult result)
        {
            if (result == SyncResult.Sent) {
                if (_settings.FeedbackSync) {
                    FeedbackService.Instance.Vibrate(TimeSpan.FromMilliseconds(90), 100);
                }
                SetStatus("Sync SENT");
            }
            else if(result == SyncResult.Delayed) {
                SetStatus("Sync BUFFERED");
            }
        }

        private void OnSynced(object sender, bool success)
        {
            if (!success) {
                SetStatus("Sync FAILED");
            }
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
