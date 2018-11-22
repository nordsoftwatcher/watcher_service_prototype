using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SiWatchApp.Configuration;
using SiWatchApp.Models;
using SiWatchApp.Monitors;
using SiWatchApp.Queue;
using SiWatchApp.Services;
using SiWatchApp.Utils;
using Tizen.Wearable.CircularUI.Forms;
using Xamarin.Forms;

namespace SiWatchApp
{
    public class MainPage : AppPage
    {
        private readonly SettingsService _settingsService;
        private ProfileService _profileService;
        
        private Label deviceIdLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Label apiUrlLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Label stateLabel = new Label() { FontSize = 5, TextColor = Color.DeepPink, HorizontalOptions = LayoutOptions.Center };

        private Settings _settings;
        private Profile _profile;
        private IPriorityQueue _queue;
        private MonitoringPolicyService _monitoringPolicyService;
        private MonitoringCollector _monitoringCollector;
        private DataService _dataService;
        private SyncService _syncService;
        private PermissionManager _permissionManager;
        private MonitorFactory _monitorFactory;

        public MainPage(SettingsService settingsService)
        {
            _settingsService = settingsService;
            var layout = new StackLayout() {
                    VerticalOptions = LayoutOptions.Center,
                    Children = { deviceIdLabel, apiUrlLabel, stateLabel }
            };
            Content = layout;
        }

        public async Task Init()
        {
            _settings = await _settingsService.GetSettings();
            deviceIdLabel.Text = $"DeviceID={_settings.DeviceId}";
            apiUrlLabel.Text = $"APIUrl={_settings.ApiUrl}";

            try {
                stateLabel.Text = "Checking permissions...";
                _permissionManager = new PermissionManager();
                await _permissionManager.Demand(new[] { "http://tizen.org/privilege/internet" });

                stateLabel.Text = "Initializing monitors...";
                _monitorFactory = new MonitorFactory(_permissionManager);
                await _monitorFactory.Init();

                stateLabel.Text = "Loading profile...";
                _profileService = new ProfileService(_settings);
                _profile = await _profileService.GetProfile();

                stateLabel.Text = "Initializing services...";
                _queue = new InMemoryPriorityQueue();
                _monitoringPolicyService = new MonitoringPolicyService(_profile);
                _monitoringCollector = new MonitoringCollector(_monitoringPolicyService, _monitorFactory, _queue);
                _dataService = new DataService(_settings);
                _syncService = new SyncService(_queue, _monitoringPolicyService, _dataService, _settings);

                stateLabel.Text = "Collecting and sending data...";
                _monitoringCollector.Start();
                _syncService.Start();
            }
            catch (Exception ex) {
                Toast.DisplayText(ex.ToString());
            }
        }




    }
}
