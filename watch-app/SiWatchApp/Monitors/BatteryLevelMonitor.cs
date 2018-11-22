using System;
using Tizen.System;

namespace SiWatchApp.Monitors
{
    public class BatteryLevelMonitor : MonitorBase<int>
    {
        public override MonitorType MonitorType => MonitorType.Battery;

        public override MonitorValue GetCurrentValue()
        {
            return new MonitorValue(Battery.Percent);
        }
    }
}
