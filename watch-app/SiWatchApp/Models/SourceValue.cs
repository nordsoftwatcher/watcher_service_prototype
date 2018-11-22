using System;

namespace SiWatchApp.Models
{
    public class SourceValue
    {
        public SourceValue(object value)
        {
            Value = value;
            Timestamp = DateTime.Now;
        }

        public object Value { get; }

        public DateTime Timestamp { get; }

        public override string ToString()
        {
            return $"{GetType().Name}{{Timestamp={Timestamp},Value={Value}}}";
        }
    }
}
