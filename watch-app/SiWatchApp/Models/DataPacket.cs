using System.Collections.Generic;
using SiWatchApp.Events;
using SiWatchApp.Monitors;

namespace SiWatchApp.Models
{
    public class DataPacket
    {
        public string DeviceId { get; set; }
        public IDictionary<MonitorType, List<SourceValue>> Monitoring { get; set; }
        public List<EventRecord> Events { get; set; }
    }
}
