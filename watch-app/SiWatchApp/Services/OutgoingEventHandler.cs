using System;
using System.Threading.Tasks;
using SiWatchApp.Buffer;
using SiWatchApp.Events;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.UI;

namespace SiWatchApp.Services
{
    public class OutgoingEventHandler : IObserver<EventRecord>
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(OutgoingEventHandler));

        private readonly IBuffer<Record> _buffer;
        private readonly SyncService _syncService;

        public OutgoingEventHandler(IBuffer<Record> buffer, SyncService syncService)
        {
            _buffer = buffer;
            _syncService = syncService;
        }

        private Priority MapPriority(EventPriority eventPriority)
        {
            switch (eventPriority)
            {
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
            try {
                _buffer.Append(eventRecord, MapPriority(eventRecord.Priority));
            }
            catch (Exception ex) {
                LOGGER.Error("Buffer error (Append):", ex);
                throw;
            }

            if (eventRecord.Priority == EventPriority.Urgent) {
                try {
                    await _syncService.ForceSync();
                }
                catch (Exception) {
                    //Notification.ShowToast($"Delayed sending {eventRecord.EventType}!", TimeSpan.FromSeconds(1));
                    return;
                }
                //Notification.ShowToast($"{eventRecord.EventType} sent!", TimeSpan.FromMilliseconds(500));
            }
        }

        public void OnCompleted()
        {
            LOGGER.Info("Event completed");
        }

        public void OnError(Exception error)
        {
            LOGGER.Warn("Event error:", error);
        }

        public void OnNext(EventRecord eventRecord)
        {
            LOGGER.Info($"{eventRecord}");
            if (eventRecord != null) {
                HandleEvent(eventRecord);
            }
        }

    }
}
