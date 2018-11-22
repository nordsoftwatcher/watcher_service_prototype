using System;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using SiWatchApp.Models;
using SiWatchApp.Queue;

namespace SiWatchApp.Events
{
    public class SOSInfo : PositionInfo
    {
        public string Message { get; set; }
    }

    public class SOSEventSource : IEventSource
    {
        private readonly Subject<EventValue> _source;
        
        public SOSEventSource()
        {
            _source = new Subject<EventValue>();
            Events = _source.AsObservable();
        }

        public void Signal(string message)
        {
            _source.OnNext(new EventValue(new SOSInfo() { Message = message }));
        }

        public IObservable<EventValue> Events { get; }

        public EventType EventType => EventType.SOS;

        public Priority Priority => Priority.Highest;
    }
}
