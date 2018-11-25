using System.Collections.Generic;

namespace SiWatchApp.Models
{
    public class SyncPacket : DataPacket
    {
        public IList<MonitorRecord> Monitors { get; set; }
        public IList<EventRecord> Events { get; set; }
    }
}
