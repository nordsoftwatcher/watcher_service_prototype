using System;
using System.Collections.Generic;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Threading.Tasks;
using SiWatchApp.Events;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Queue;
using Tizen.Wearable.CircularUI.Forms;
using Xamarin.Forms;

namespace SiWatchApp.Services
{
    public class EventProcessor : IObserver<> IDisposable
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(EventProcessor));

        private readonly SyncService _syncService;
        private readonly IDisposable _subscription;

        public EventProcessor(
                IList<IObservable<EventRecord>> events,
                SyncService syncService,
                IScheduler scheduler = null)
        {
            _outputQueue = outputQueue;
            _syncClient = syncClient;
            _subscription = eventSources.Merge(scheduler ?? TaskPoolScheduler.Default).Subscribe(HandleEvent);
        }

        private Priority GetQueuePriority(EventPriority eventPriority)
        {
            switch (eventPriority) {
                case EventPriority.Urgent:
                    return Priority.Highest;
                case EventPriority.High:
                    return Priority.High;
                case EventPriority.Normal:
                    return Priority.Normal;
                case EventPriority.Low:
                    return Priority.Low;
                default:
                    throw new ArgumentOutOfRangeException(nameof(eventPriority));
            }
        }

        private async void HandleEvent(EventRecord eventRecord)
        {
            if (eventRecord.Priority == EventPriority.Urgent) {
                LOGGER.Info("Sending urgent event:", eventRecord);
                try {
                    await _syncClient.Send(new SyncPacket { Events = new List<EventRecord> { eventRecord } });
                    Device.BeginInvokeOnMainThread(() => { Toast.DisplayText("Event sent!", 1000); });
                    return;
                }
                catch (Exception ex) {
                    LOGGER.Error("Failed sending event. Will be sent on next flush:", ex);
                }
            }
            await _outputQueue.Put(eventRecord, GetQueuePriority(eventRecord.Priority));
        }

        public void Dispose()
        {
            _subscription.Dispose();
        }
    }
}
