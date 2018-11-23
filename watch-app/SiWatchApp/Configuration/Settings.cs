using System;

namespace SiWatchApp.Configuration
{
    public class Settings
    {
        public String DeviceId { get; set; }
        public String ApiUrl { get; set; }
        public int SendRetryCount { get; set; }
        public TimeSpan SendRetryDelay { get; set; }
    }
}
