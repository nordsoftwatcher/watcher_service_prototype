using System;
using Newtonsoft.Json;

namespace SiWatchApp.Configuration
{
    public class BatteryLevelAwareMonitoringPolicy : MonitoringPolicy
    {
        public int MinBatteryLevel { get; set; }

        public override string ToString()
        {
            return $"MonitoringPolicy{{MinBatteryLevel={MinBatteryLevel},FlushInterval={FlushInterval},PacketSize={PacketSize},Monitors=[{String.Join(",", Monitors)}]}}";
        }
    }
}
