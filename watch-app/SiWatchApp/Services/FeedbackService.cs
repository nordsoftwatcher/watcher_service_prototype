using System;
using System.Threading.Tasks;
using SiWatchApp.Logging;
using Tizen.System;

namespace SiWatchApp.Services
{
    public class FeedbackService
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(FeedbackService));

        public static readonly FeedbackService Instance = new FeedbackService();

        public static string Privilege => "http://tizen.org/privilege/haptic";
        
        public void Vibrate(TimeSpan duration, int intensity)
        {
            if (Vibrator.Vibrators.Count > 0) {
                var vibrator = Vibrator.Vibrators[0];
                try {
                    vibrator.Vibrate((int) duration.TotalMilliseconds, intensity);
                }
                catch (Exception ex) {
                    LOGGER.Warn("Vibrator error", ex);
                }
            }
            else {
                LOGGER.Info("No vibrators available");
            }
        }
    }
}
