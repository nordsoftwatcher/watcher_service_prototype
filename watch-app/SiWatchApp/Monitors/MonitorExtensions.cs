using System;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Threading;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Utils;

namespace SiWatchApp.Monitors
{
    public static class MonitorExtensions
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(MonitorExtensions));

        public static IObservable<MonitorRecord> Observe(this IMonitor monitor, TimeSpan interval, IScheduler scheduler = null)
        {
            return Observable.Interval(interval, scheduler ?? TaskPoolScheduler.Default)
                             .Do(_ => {
                                     LOGGER.Debug($"Polling {monitor} on thread #{Thread.CurrentThread.ManagedThreadId}");
                                 })   
                             .Select(_ => monitor.GetCurrentValue())
                             .Do(v => {
                                     if (v == null) {
                                         LOGGER.Debug($"{monitor} returned empty value");
                                     }
                                 },
                                 ex => {
                                     LOGGER.Warn($"Failed getting {monitor} value", ex);
                                 })
                             .Catch(Observable.Empty<object>())
                             .Where(v => v != null)
                             .Select(v => new MonitorRecord(monitor.MonitorType, v));
        }
    }
}
