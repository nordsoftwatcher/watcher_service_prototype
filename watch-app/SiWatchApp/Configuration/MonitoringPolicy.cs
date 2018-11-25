using System;
using System.Collections.Generic;
using Newtonsoft.Json;

namespace SiWatchApp.Configuration
{
    public class MonitoringPolicy
    {
        public int MinBatteryLevel { get; set; }

        public List<MonitorConfig> Monitors { get; set; }

        public int SyncInterval { get; set; }

        public int PacketSize { get; set; }

        public override string ToString()
        {
            return $"MonitoringPolicy{{MinBatteryLevel={MinBatteryLevel},SyncInterval={SyncInterval},PacketSize={PacketSize},Monitors=[{String.Join(",", Monitors)}]}}";
        }
    }
}
