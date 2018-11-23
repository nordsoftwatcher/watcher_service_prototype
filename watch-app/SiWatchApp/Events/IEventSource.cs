using System;
using SiWatchApp.Models;
using SiWatchApp.Queue;

namespace SiWatchApp.Events
{
    public interface IEventSource : IRequirePrivileges, IDisposable
    {
        EventType EventType { get; }
        IObservable<EventValue> Events { get; }
        Priority Priority { get; }
        void Init();
    }
}
