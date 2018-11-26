using Newtonsoft.Json;
using SiWatchApp.Events;

namespace SiWatchApp.Models
{
    public class EventRecord : Record
    {
        public EventType EventType { get; set; }

        public LocationInfo Location { get; set; }

        public EventPriority Priority { get; set; }
        
        public override string ToString()
        {
            return $"EventRecord{{EventType={EventType},Timestamp={Timestamp},Value={Value},Location={Location}}}";
        }
    }
}
