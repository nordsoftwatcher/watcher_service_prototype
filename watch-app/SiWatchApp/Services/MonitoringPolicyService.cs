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

        public MonitoringPolicyService(Profile profile)
        {
            _policies = profile.Monitoring.Where(m => m != null).OrderByDescending(m => m.MinBatteryLevel).ToList();
            CurrentMonitoringPolicy = ChooseMonitoringPolicy();
            LogCurrentMonitoringPolicy();

            Battery.PercentChanged += Battery_PercentChanged;
        }

        private MonitoringPolicy ChooseMonitoringPolicy()
        {
            return _policies.FirstOrDefault(m => m.MinBatteryLevel < Battery.Percent);
        }

        private void LogCurrentMonitoringPolicy()
        {
            var currentMonitoringPolicyName = CurrentMonitoringPolicyName;
            LOGGER.Info(currentMonitoringPolicyName == null ? "Monitoring off" : $"Current monitoring policy is '{currentMonitoringPolicyName}%'");
        }

        private void Battery_PercentChanged(object sender, BatteryPercentChangedEventArgs e)
        {
            var mp = ChooseMonitoringPolicy();
            if (!ReferenceEquals(CurrentMonitoringPolicy, mp)) {
                CurrentMonitoringPolicy = mp;
                LogCurrentMonitoringPolicy();
                MonitoringPolicyChanged?.Invoke(this, mp);
            }
        }

        public string CurrentMonitoringPolicyName
        {
            get {
                var policy = CurrentMonitoringPolicy;
                var index = _policies.FindIndex(p => p == policy);
                if (index < 0) {
                    return null;
                }
                if (index == 0) {
                    return "100~" + policy.MinBatteryLevel;
                }
                return _policies[index - 1].MinBatteryLevel + "~" + policy.MinBatteryLevel;
            }
        }

        public MonitoringPolicy CurrentMonitoringPolicy { get; private set; }

        public event EventHandler<MonitoringPolicy> MonitoringPolicyChanged;
    }
}
