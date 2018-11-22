using System;
using System.Collections.Generic;
using Newtonsoft.Json;

namespace SiWatchApp.Configuration
{
    public class MonitoringPolicy
    {
        public List<MonitorConfig> Monitors { get; set; }

        public int FlushInterval { get; set; }

        public int PacketSize { get; set; }
    }
}
