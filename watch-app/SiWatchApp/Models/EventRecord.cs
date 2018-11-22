using SiWatchApp.Events;

namespace SiWatchApp.Models
{
    public class EventRecord : DataRecord
    {
        public EventType EventType { get; }

        public EventRecord(EventType eventType, EventValue eventValue)
        {
            EventType = eventType;
            Value = eventValue;
        }

        public override string ToString()
        {
            return $"EventRecord{{EventType={EventType}, Value={Value}}}";
        }
    }
}
