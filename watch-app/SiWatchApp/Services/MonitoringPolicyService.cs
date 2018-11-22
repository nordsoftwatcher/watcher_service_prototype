using System;
using System.Linq;
using SiWatchApp.Configuration;
using Tizen.System;

namespace SiWatchApp.Services
{
    public class MonitoringPolicyService
    {
        private readonly object _sync = new object();
        private readonly Profile _profile;
        private MonitoringPolicy _currentMonitoringPolicy;

        public MonitoringPolicyService(Profile profile)
        {
            _profile = profile;
            _currentMonitoringPolicy = ChooseMonitoringPolicy();
            Battery.PercentChanged += Battery_PercentChanged;
        }

        private MonitoringPolicy ChooseMonitoringPolicy()
        {
            return _profile.Monitoring.Where(m => m != null).OrderByDescending(m => m.MinBatteryLevel)
                             .FirstOrDefault(m => m.MinBatteryLevel < Battery.Percent);
        }

        private void Battery_PercentChanged(object sender, BatteryPercentChangedEventArgs e)
        {
            var mp = ChooseMonitoringPolicy();
            if (!ReferenceEquals(CurrentMonitoringPolicy, mp)) {
                CurrentMonitoringPolicy = mp;
                MonitoringPolicyChanged?.Invoke(this, mp);
            }
        }

        public MonitoringPolicy CurrentMonitoringPolicy {
            get {
                lock (_sync) {
                    return _currentMonitoringPolicy;
                }
            }
            private set
            {
                lock (_sync) {
                    _currentMonitoringPolicy = value;
                }
            }
        }

        public event EventHandler<MonitoringPolicy> MonitoringPolicyChanged;
    }
}
