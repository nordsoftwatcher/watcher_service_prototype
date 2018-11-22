using System;
using System.Collections.Generic;
using System.Linq;

namespace SiWatchApp.Configuration
{
    public class Profile
    {
        public List<BatteryLevelAwareMonitoringPolicy> Monitoring { get; set; }

        public override string ToString()
        {
            return $"Profile{{Monitoring=[{String.Join(",", Monitoring)}]}}";
        }
    }
}
