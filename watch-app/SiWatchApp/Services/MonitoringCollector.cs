using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Threading;
using System.Threading.Tasks;
using SiWatchApp.Configuration;
using SiWatchApp.Models;
using SiWatchApp.Monitors;
using SiWatchApp.Queue;
using SiWatchApp.Utils;
using Tizen.System;

namespace SiWatchApp.Services
{
    public class MonitoringCollector
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(MonitoringCollector));

        private readonly MonitoringPolicyService _monitoringPolicyService;
        private readonly MonitorFactory _monitorFactory;
        private readonly IPriorityQueue _queue;
        private readonly List<IDisposable> _subscriptions = new List<IDisposable>();
        private readonly object _sync = new object();

        public MonitoringCollector(MonitoringPolicyService monitoringPolicyService, MonitorFactory monitorFactory, IPriorityQueue queue)
        {
            _queue = queue;
            _monitoringPolicyService = monitoringPolicyService;
            _monitorFactory = monitorFactory;
            _monitoringPolicyService.MonitoringPolicyChanged += HandleMonitoringPolicyChanged;
        }

        private void HandleMonitoringPolicyChanged(object sender, MonitoringPolicy mp)
        {
            Reconfigure(mp);
        }

        private void Dismiss()
        {
            _subscriptions.ForEach(s => s.Dispose());
            _subscriptions.Clear();
        }

        private async Task ProcessMonitorValue(IMonitor monitor)
        {
            LOGGER.Info($"Polling {monitor} from thread {Thread.CurrentThread.ManagedThreadId}");

            MonitorValue mv;
            try {
                mv = monitor.GetCurrentValue();
            }
            catch (Exception ex) {
                LOGGER.Error($"Failed getting current value from {monitor}", ex);
                return;
            }

            if (mv == null) {
                LOGGER.Info($"{monitor} returned empty value");
            }
            else {
                var record = new MonitoringRecord(monitor.MonitorType, mv);
                LOGGER.Debug($"{record}");
                await _queue.Put(record);
            }
        }

        private void Reconfigure(MonitoringPolicy mp)
        {
            lock (_sync) {
                Dismiss();

                foreach (MonitorConfig mc in mp.Monitors)
                {
                    var monitor = _monitorFactory.GetMonitor(mc.Type);
                    try {
                        if (!monitor.Start()) {
                            LOGGER.Warn($"{monitor} is not supported");
                            continue;
                        }
                    }
                    catch (Exception ex) {
                        LOGGER.Error($"{monitor} failed to start:", ex);
                        continue;
                    }

                    var sub = Observable.Interval(TimeSpan.FromSeconds(mc.PollInterval), TaskPoolScheduler.Default)
                                        .Subscribe(async _ => await ProcessMonitorValue(monitor));
                    _subscriptions.Add(sub);
                }
            }
        }
        
        public void Start()
        {
            Reconfigure(_monitoringPolicyService.CurrentMonitoringPolicy);
            LOGGER.Debug("Started");
        }

        public void Pause()
        {
            throw new NotImplementedException();
        }

        public void Stop()
        {
            lock (_sync) {
                Dismiss();
            }
            LOGGER.Debug("Stopped");
        }
    }
}
