using System;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Services;
using SiWatchApp.Utils;

namespace SiWatchApp.Events
{
    public abstract class AbstractEventSource : IEventSource
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger("EventSource");

        private readonly EventType _eventType;
        private readonly ILocationProvider _locationProvider;
        private Subject<EventRecord> _source;

        protected AbstractEventSource(EventType eventType, ILocationProvider locationProvider)
        {
            _eventType = eventType;
            _locationProvider = locationProvider;
            _source = new Subject<EventRecord>();
        }
        
        public virtual void Init()
        {
        }

        protected virtual void Emit(object value, EventPriority priority = EventPriority.Normal)
        {
            EventRecord record = new EventRecord() {
                    EventType = _eventType,
                    Priority = priority,
                    Value = value
            };

            if (_locationProvider != null) {
                try {
                    var location = _locationProvider.GetCurrentLocation();
                    record.Location = location != null ? new LocationInfo(location) : null;
                }
                catch (Exception ex) {
                    LOGGER.Warn("Failed getting current location", ex);
                }
            }

            _source.OnNext(record);
        }

        public void Dispose()
        {
            _source?.Dispose();
            _source = null;
        }

        public IDisposable Subscribe(IObserver<EventRecord> observer)
        {
            if (_source == null) {
                throw new ObjectDisposedException($"{GetType().Name} is disposed");
            }
            return _source.Subscribe(observer);
        }
    }
}

