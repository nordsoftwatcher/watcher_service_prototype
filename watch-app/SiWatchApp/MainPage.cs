using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using SiWatchApp.Configuration;
using SiWatchApp.Events;
using SiWatchApp.Models;
using SiWatchApp.Monitors;
using SiWatchApp.Queue;
using SiWatchApp.Services;
using SiWatchApp.Utils;
using Tizen.Wearable.CircularUI.Forms;
using Xamarin.Forms;
using Application = Tizen.Applications.Application;

namespace SiWatchApp
{
    public class MainPage : AppPage
    {
        private readonly SettingsService _settingsService;
        private ProfileService _profileService;

        private Label deviceIdLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Label apiUrlLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Button sosButton = new Button() { FontSize = 12, BackgroundColor = Color.Red, Text = "SOS", HorizontalOptions = LayoutOptions.Center, IsVisible = false };
        private Label statusLabel = new Label() { FontSize = 5, TextColor = Color.DeepPink, HorizontalOptions = LayoutOptions.Center };
        private Label syncLabel = new Label() { FontSize = 5, TextColor = Color.Green, HorizontalOptions = LayoutOptions.Center, Text = "Sync none"};
        private Button exitButton = new Button() { FontSize = 10, HorizontalOptions = LayoutOptions.Center, Text = "Exit", IsVisible = false};

        private Settings _settings;
        private Profile _profile;
        private IPriorityQueue _outputQueue;
        private IPriorityQueue _inputQueue;
        private MonitoringPolicyService _monitoringPolicyService;
        private MonitoringService _monitoringService;
        private DataService _dataService;
        private SyncService _syncService;
        private PermissionManager _permissionManager;
        private MonitorFactory _monitorFactory;
        private EventProcessor _eventProcessor;
        private SOSEventSource _sosEventSource;
        private EventSourceFactory _eventSourceFactory;

        private SynchronizationContext _sx;

        public MainPage(SettingsService settingsService)
        {
            _settingsService = settingsService;
            
            var layout = new StackLayout() {
                    VerticalOptions = LayoutOptions.Center,
                    Children = { sosButton, deviceIdLabel, apiUrlLabel, statusLabel, syncLabel, exitButton }
            };
            Content = layout;
        }

        private void SosButton_Clicked(object sender, EventArgs e)
        {
            _sosEventSource?.Signal("SOS!");
        }

        public async Task Init()
        {
            _sx = SynchronizationContext.Current;

            _settings = await _settingsService.GetSettings();
            deviceIdLabel.Text = $"DeviceID: {_settings.DeviceId}";
            apiUrlLabel.Text = $"API: {_settings.ApiUrl}";
            
            try {
                statusLabel.Text = "Checking permissions...";
                _permissionManager = new PermissionManager();
                await _permissionManager.Demand(new[] { "http://tizen.org/privilege/internet" });
                
                statusLabel.Text = "Loading profile...";
                _profileService = new ProfileService(_settings);
                _profile = await _profileService.GetProfile();

                
                statusLabel.Text = "Initializing services...";
                _monitorFactory = new MonitorFactory(_permissionManager);
                _eventSourceFactory = new EventSourceFactory(_permissionManager);

                _outputQueue = new InMemoryPriorityQueue();
                _inputQueue = new InMemoryPriorityQueue();

                _monitoringPolicyService = new MonitoringPolicyService(_profile);
                _monitoringService = new MonitoringService(_monitoringPolicyService, _monitorFactory, _outputQueue);
                _dataService = new DataService(_settings);

                _sosEventSource = await _eventSourceFactory.GetEventSource(EventType.SOS) as SOSEventSource;
                _eventProcessor = new EventProcessor(new List<IEventSource> { _sosEventSource }, _outputQueue, _dataService);

                _syncService = new SyncService(_inputQueue, _outputQueue, _monitoringPolicyService, _dataService, _settings);
                _syncService.Synced += HandleSynced;

                statusLabel.Text = "Collecting and sending data...";

                _monitoringService.Start();
                _syncService.Start();

                sosButton.IsVisible = true;
                sosButton.Clicked += SosButton_Clicked;
            }
            catch (Exception ex) {
                statusLabel.Text = "Error";
                Toast.DisplayText(ex.ToString());
            }
            
            exitButton.IsVisible = true;
            exitButton.Clicked += ExitButton_Clicked;
        }

        private void ExitButton_Clicked(object sender, EventArgs e)
        {
            this._syncService.Stop();
            this._sosEventSource.Dispose();
            this._eventProcessor.Dispose();
            this._monitoringService.Stop();
            this._monitorFactory.Dispose();

            Application.Current.Exit();
        }

        private int syncCount = 0;
        

        private void HandleSynced(object sender, EventArgs e)
        {
            _sx.Post(_ => { syncLabel.Text = $"Sync {++syncCount} times"; }, null);
        }
    }
}
