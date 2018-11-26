using System;
using System.Threading.Tasks;
using SiWatchApp.Events;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.UI;
using Tizen.Wearable.CircularUI.Forms;

namespace SiWatchApp.Services
{
    public class IncomingEventHandler : IObserver<EventRecord>
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(IncomingEventHandler));

        private void HandleEvent(EventRecord eventRecord)
        {
            if (eventRecord.EventType == EventType.Message) {
                FeedbackService.Instance.Vibrate(TimeSpan.FromMilliseconds(500), 80);
                Notification.ShowToast("Incoming message: "+(eventRecord.Value != null ? eventRecord.Value.ToString() : "<NULL>"), TimeSpan.FromSeconds(3));
                //Notification.ShowInfo("Incoming message", eventRecord.Value != null ? eventRecord.Value.ToString() : "<NULL>");
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
