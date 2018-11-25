using System;
using System.Collections.Generic;
using SiWatchApp.Events;
using SiWatchApp.Monitors;

namespace SiWatchApp.Models
{
    public abstract class DataPacket
    {
        public string DeviceId { get; set; }
        public DateTime Timestamp { get; set; } = DateTime.Now;
    }
}
