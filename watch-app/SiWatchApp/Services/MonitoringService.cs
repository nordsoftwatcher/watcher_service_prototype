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
    public class MonitoringService
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(MonitoringService));

        private readonly MonitoringPolicyService _monitoringPolicyService;
        private readonly MonitorFactory _monitorFactory;
        private readonly IPriorityQueue _queue;
        private readonly List<IDisposable> _subscriptions = new List<IDisposable>();
        private readonly object _sync = new object();

        public MonitoringService(MonitoringPolicyService monitoringPolicyService, MonitorFactory monitorFactory, IPriorityQueue queue)
        {
            _queue = queue;
            _monitoringPolicyService = monitoringPolicyService;
            _monitorFactory = monitorFactory;
            _monitoringPolicyService.MonitoringPolicyChanged += HandleMonitoringPolicyChanged;
        }

        private async void HandleMonitoringPolicyChanged(object sender, MonitoringPolicy mp)
        {
            await Reconfigure(mp);
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

        private async Task Reconfigure(MonitoringPolicy mp)
        {
            lock (_sync) {
                Dismiss();
            }

            var monitors = new List<Tuple<MonitorConfig, IMonitor>>();
            foreach (MonitorConfig mc in mp.Monitors) {
                IMonitor monitor;
                try {
                    monitor = await _monitorFactory.GetMonitor(mc.Type);
                }
                catch (Exception ex) {
                    LOGGER.Error($"{mc.Type} failed to start:", ex);
                    continue;
                }

                monitors.Add(Tuple.Create(mc, monitor));
            }

            lock (_sync) {
                if (_subscriptions.Count == 0) {
                    monitors.ForEach(m => {
                                         var sub = Observable
                                                   .Interval(TimeSpan.FromSeconds(m.Item1.PollInterval),
                                                             TaskPoolScheduler.Default)
                                                   .Subscribe(async _ => await ProcessMonitorValue(m.Item2));
                                         _subscriptions.Add(sub);
                                     });
                }
            }
        }
    
        
        public async void Start()
        {
            await Reconfigure(_monitoringPolicyService.CurrentMonitoringPolicy);
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
