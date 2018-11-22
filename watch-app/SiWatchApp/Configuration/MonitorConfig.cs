using Newtonsoft.Json;
using SiWatchApp.Monitors;

namespace SiWatchApp.Configuration
{
    public class MonitorConfig
    {
        public MonitorType Type { get; set; }

        public int PollInterval { get; set; }

        public override string ToString()
        {
            return $"MonitorConfig{{Type={Type},PollInterval={PollInterval}}}";
        }
    }
}
