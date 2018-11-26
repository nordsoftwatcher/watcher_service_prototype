using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using SiWatchApp.Events;
using SiWatchApp.Logging;
using SiWatchApp.Monitors;
using SiWatchApp.Utils;
using Xamarin.Forms.Internals;

namespace SiWatchApp.Services
{
    public class EventSourceFactory : IDisposable
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(EventSourceFactory));

        private readonly object _sync = new object();
        private Dictionary<EventType, IEventSource> _eventSources = new Dictionary<EventType, IEventSource>();
        private readonly PermissionManager _permissionManager;

        public EventSourceFactory(PermissionManager permissionManager)
        {
            _permissionManager = permissionManager;
        }
        
        public async Task<IEventSource> GetEventSource(EventType type)
        {
            bool check = false;
            IEventSource eventSource = null;
            lock (_sync)
            {
                if (_eventSources == null) {
                    throw new ObjectDisposedException("EventSourceFactory is disposed");
                }
                if (_eventSources.ContainsKey(type)) {
                    eventSource = _eventSources[type];
                }
                else {
                    try {
                        switch (type) {
                            case EventType.SOS:
                                eventSource = new SOSEventSource();
                                break;
                            case EventType.Message:
                                eventSource = new AbstractEventSource();
                                break;
                        }
                    }
                    catch (Exception ex)
                    {
                        LOGGER.Error($"Failed creating event source of type '{type}':", ex);
                        throw;
                    }

                    if (eventSource != null) {
                        _eventSources.Add(type, eventSource);
                        check = true;
                    }
                }
            }
            if (check) {
                await _permissionManager.Demand(eventSource.Privileges);
                eventSource.Init();
            }
            return eventSource;
        }

        public void Dispose()
        {
            lock (_sync)
            {
                _eventSources.Values.ForEach(es => es.Dispose());
                _eventSources = null;
            }
        }
    }
}
