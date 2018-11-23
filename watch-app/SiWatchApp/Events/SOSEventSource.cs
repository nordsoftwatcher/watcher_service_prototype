using System;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using SiWatchApp.Models;
using SiWatchApp.Monitors;
using SiWatchApp.Queue;
using SiWatchApp.Utils;

namespace SiWatchApp.Events
{
    public class SOSEventSource : MessageEventSource
    {
        public override EventType EventType => EventType.SOS;

        public override Priority Priority => Priority.Highest;
    }
}
