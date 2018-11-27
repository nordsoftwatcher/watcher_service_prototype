using System;
using System.Threading.Tasks;
using SiWatchApp.Events;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Queue;
using SiWatchApp.UI;
using Tizen.Wearable.CircularUI.Forms;

namespace SiWatchApp.Services
{
    public class IncomingEventHandler : IObserver<EventRecord>
    {
        private readonly IPriorityQueue<TextMessage> _messageQueue;
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(IncomingEventHandler));

        public IncomingEventHandler(IPriorityQueue<TextMessage> messageQueue)
        {
            _messageQueue = messageQueue;
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

        private void HandleEvent(EventRecord eventRecord)
        {
            if (eventRecord.EventType == EventType.Message) {
                FeedbackService.Instance.Vibrate(TimeSpan.FromMilliseconds(500), 80);
                var sms = new TextMessage {
                    Text = (eventRecord.Value != null ? eventRecord.Value.ToString() : "<NULL>")
                };
                _messageQueue.Put(sms, MapPriority(eventRecord.Priority));

                LOGGER.Info("Got message");

                //Notification.ShowToast("Incoming message: "+(eventRecord.Value != null ? eventRecord.Value.ToString() : "<NULL>"), TimeSpan.FromSeconds(3));
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
