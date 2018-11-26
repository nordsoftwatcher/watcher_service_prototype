using System;
using System.Collections.Generic;
using SiWatchApp.Buffer;
using SiWatchApp.Configuration;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Monitors;

namespace SiWatchApp.Services
{
    public class MonitoringService : IDisposable
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(MonitoringService));

        private readonly object _sync = new object();

        private readonly MonitoringPolicyService _monitoringPolicyService;
        private readonly IBuffer<Record> _buffer;

        private MonitorFactory _monitorFactory = new MonitorFactory();
        private readonly List<IDisposable> _subscriptions = new List<IDisposable>();
        private bool _started = false;

        public MonitoringService(MonitoringPolicyService monitoringPolicyService, IBuffer<Record> buffer)
        {
            _buffer = buffer;
            _monitoringPolicyService = monitoringPolicyService;
        }

        private void HandleMonitoringPolicyChanged(object sender, MonitoringPolicy mp)
        {
            Reconfigure(mp);
        }

        private void Unsubscribe()
        {
            _subscriptions.ForEach(s => s.Dispose());
            _subscriptions.Clear();
        }

        private void ProcessMonitorValue(MonitorRecord record)
        {
            LOGGER.Debug($"{record}");
            _buffer.Append(record, Priority.Normal);
        }

        private void Reconfigure(MonitoringPolicy policy)
        {
            lock (_sync) {
                Unsubscribe();

                if (policy == null) {
                    return;
                }

                if (_monitorFactory == null) {
                    return;
                }

                foreach (var monitorConfig in policy.Monitors) {
                    IMonitor monitor;
                    try {
                        monitor = _monitorFactory.GetMonitor(monitorConfig.Type);
                    }
                    catch (Exception ex) {
                        LOGGER.Error($"{monitorConfig.Type} failed to start:", ex);
                        continue;
                    }

                    if (monitor != null) {
                        _subscriptions.Add(monitor.Observe(TimeSpan.FromSeconds(monitorConfig.PollInterval)).Subscribe(ProcessMonitorValue));
                    }
                }
            }
        }
    
        
        public void Start()
        {
            lock (_sync) {
                if (_started) {
                    return;
                }
                _started = true;
                _monitoringPolicyService.MonitoringPolicyChanged += HandleMonitoringPolicyChanged;
                Reconfigure(_monitoringPolicyService.CurrentMonitoringPolicy);
                LOGGER.Debug("Started");
            }
        }

        public void Pause()
        {
            throw new NotImplementedException();
        }

        public void Stop()
        {
            lock (_sync) {
                if (_started) {
                    _started = false;
                    _monitoringPolicyService.MonitoringPolicyChanged -= HandleMonitoringPolicyChanged;
                    Unsubscribe();
                    LOGGER.Debug("Stopped");
                }
            }
        }

        public void Dispose()
        {
            lock (_sync) {
                _started = false;
                _monitoringPolicyService.MonitoringPolicyChanged -= HandleMonitoringPolicyChanged;
                Unsubscribe();
                if (_monitorFactory != null) {
                    _monitorFactory.Dispose();
                    _monitorFactory = null;
                    LOGGER.Debug("Disposed");
                }
            }
        }
    }
}
