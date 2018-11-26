using SiWatchApp.Monitors;
using SiWatchApp.Services;

namespace SiWatchApp.Events
{
    public class SOSEventSource : AbstractEventSource
    {
        public SOSEventSource(ILocationProvider locationProvider = null) : base(EventType.SOS, locationProvider) { }

        public void Signal(string message)
        {
            Emit(message, EventPriority.Urgent);
        }
    }
}
