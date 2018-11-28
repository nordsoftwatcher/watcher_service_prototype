using System;

namespace SiWatchApp.Configuration
{
    public class Settings
    {
        public String DeviceId { get; set; }
        public String ApiUrl { get; set; }
        public bool FeedbackSync { get; set; }
        public int SyncSendRetryCount { get; set; }
        public TimeSpan SyncSendRetryDelay { get; set; }
        public TimeSpan SyncSendHttpTimeout { get; set; }
        public int DefaultSyncPacketSize { get; set; }
    }
}
