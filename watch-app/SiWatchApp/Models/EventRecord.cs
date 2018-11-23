using Newtonsoft.Json;
using SiWatchApp.Events;

namespace SiWatchApp.Models
{
    public class EventRecord : DataRecord
    {
        public EventType EventType { get; }

        [JsonConstructor]
        public EventRecord(EventType eventType, EventValue value)
        {
            EventType = eventType;
            Value = value;
        }

        public override string ToString()
        {
            return $"EventRecord{{EventType={EventType}, Value={Value}}}";
        }
    }
}
