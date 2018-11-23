using System;
using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using System.Net.Http;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Reflection.Metadata;
using System.Threading.Tasks;
using SiWatchApp.Events;
using SiWatchApp.Models;
using SiWatchApp.Queue;
using SiWatchApp.Utils;
using Tizen.Wearable.CircularUI.Forms;

namespace SiWatchApp.Services
{
    public class EventProcessor : IDisposable
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(EventListener));

        private readonly IPriorityQueue _queue;
        private readonly DataService _dataService;
        private readonly IDisposable _subscription;

        public EventProcessor(IList<IEventSource> eventSources, IPriorityQueue queue, DataService dataService)
        {
            _queue = queue;
            _dataService = dataService;
            _subscription = eventSources.Select(es => es.Events.Select(ev => new { EventSource = es, EventValue = ev }))
                                        .Merge().Subscribe(e => HandleEvent(e.EventSource, e.EventValue));
        }

        private async void HandleEvent(IEventSource source, EventValue value)
        {
            EventRecord record = new EventRecord(source.EventType, value);
            if (source.EventType == EventType.SOS) {
                LOGGER.Info("Sending SOS");
                try {
                    await SendEvent(record);
                    Toast.DisplayText("SOS sent!");
                    return;
                }
                catch (Exception ex) {
                    LOGGER.Error("Failed sending SOS. Will be sent on next flush:", ex);
                }
            }
            await _queue.Put(record, source.Priority);
        }

        private Task SendEvent(EventRecord ev)
        {
            return _dataService.Send(new DataPacket() { Events = new List<EventRecord>() { ev } });
        }

        public void Dispose()
        {
            _subscription.Dispose();
        }
    }
}
