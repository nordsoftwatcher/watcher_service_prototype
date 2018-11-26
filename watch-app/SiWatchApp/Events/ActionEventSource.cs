using SiWatchApp.Monitors;
using SiWatchApp.Services;

namespace SiWatchApp.Events
{
    public class ActionEventSource : AbstractEventSource
    {
        public ActionEventSource(ILocationProvider locationProvider = null) : base(EventType.Action, locationProvider) { }

        public void Signal(string code, EventPriority priority = EventPriority.Normal)
        {
            Emit(code, priority);
        }
    }
}
