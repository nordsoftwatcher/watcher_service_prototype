using System;
using System.Collections.Generic;
using System.Linq;
using SiWatchApp.Configuration;
using SiWatchApp.Logging;
using Tizen.System;

namespace SiWatchApp.Services
{
    public class MonitoringPolicyService
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(MonitoringPolicyService));

        private readonly List<MonitoringPolicy> _policies;
        private MonitoringPolicy _currentMonitoringPolicy;

        public MonitoringPolicyService(Profile profile)
        {
            _policies = profile.Monitoring.Where(m => m != null).OrderByDescending(m => m.MinBatteryLevel).ToList();
            _currentMonitoringPolicy = ChooseMonitoringPolicy();
            LogCurrentMonitoringPolicy();

            Battery.PercentChanged += Battery_PercentChanged;
        }

        private MonitoringPolicy ChooseMonitoringPolicy()
        {
            return _policies.FirstOrDefault(m => m.MinBatteryLevel <= Battery.Percent);
        }

        private void LogCurrentMonitoringPolicy()
        {
            if(_currentMonitoringPolicy == null)
                LOGGER.Info("Monitoring off");
            else
                LOGGER.Info($"Monitoring policy is '>{_currentMonitoringPolicy.MinBatteryLevel}%'");
        }

        private void Battery_PercentChanged(object sender, BatteryPercentChangedEventArgs e)
        {
            var mp = ChooseMonitoringPolicy();
            if (!ReferenceEquals(_currentMonitoringPolicy, mp)) {
                _currentMonitoringPolicy = mp;
                LogCurrentMonitoringPolicy();
                MonitoringPolicyChanged?.Invoke(this, mp);
            }
        }

        public MonitoringPolicy CurrentMonitoringPolicy => _currentMonitoringPolicy;

        public event EventHandler<MonitoringPolicy> MonitoringPolicyChanged;
    }
}
