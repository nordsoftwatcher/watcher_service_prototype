using SiWatchApp.Monitors;
using SiWatchApp.Services;

namespace SiWatchApp.Events
{
    public class MessageEventSource : AbstractEventSource
    {
        public MessageEventSource(ILocationProvider locationProvider = null) : base(EventType.Message, locationProvider) { }

        public void Send(string message, EventPriority priority = EventPriority.Normal)
        {
            Emit(message, priority);
        }
    }
}
