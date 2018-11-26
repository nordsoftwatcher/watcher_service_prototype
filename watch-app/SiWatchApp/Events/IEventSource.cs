using System;
using SiWatchApp.Models;

namespace SiWatchApp.Events
{
    public interface IEventSource : IObservable<EventRecord>, IDisposable
    {
        void Init();
    }
}
