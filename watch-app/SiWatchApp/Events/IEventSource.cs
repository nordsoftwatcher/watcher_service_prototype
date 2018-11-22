using System;
using SiWatchApp.Queue;

namespace SiWatchApp.Events
{
    public interface IEventSource
    {
        EventType EventType { get; }
        IObservable<EventValue> Events { get; }
        Priority Priority { get; }
    }
}
