using System;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using SiWatchApp.Models;
using SiWatchApp.Monitors;
using SiWatchApp.Queue;
using SiWatchApp.Utils;

namespace SiWatchApp.Events
{
    public class MessageEventSource : IEventSource
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(MessageEventSource));

        private readonly LocationMonitor _locationMonitor;
        private readonly Subject<EventValue> _source;

        public MessageEventSource()
        {
            _locationMonitor = new LocationMonitor();
            _source = new Subject<EventValue>();
            Events = _source.AsObservable();
        }

        public void Init()
        {
            _locationMonitor.Start();
        }

        public virtual void Signal(string text)
        {
            var message = new MessageInfo() { Message = text };
            try {
                var value = _locationMonitor.GetCurrentValue();
                if (value != null) {
                    var location = (LocationInfo)value.Value;
                    message.Location = location;
                }
                else {
                    LOGGER.Info("Location is unavailable");
                }
            }
            catch (Exception ex) {
                LOGGER.Warn("Failed getting current location", ex);
            }

            _source.OnNext(new EventValue(message));
        }

        public IObservable<EventValue> Events { get; }

        public virtual EventType EventType => EventType.Message;

        public virtual Priority Priority => Priority.High;

        public void Dispose()
        {
            _locationMonitor?.Dispose();
            _source?.Dispose();
        }

        public string[] Privileges => _locationMonitor.Privileges;
    }
}

