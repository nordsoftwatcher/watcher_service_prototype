using System;
using System.Collections.Generic;
using SiWatchApp.Logging;
using SiWatchApp.Monitors;


namespace SiWatchApp.Services
{
    public class MonitorFactory : IDisposable
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(MonitorFactory));

        private readonly object _sync = new object();
        private Dictionary<MonitorType, IMonitor> _monitors = new Dictionary<MonitorType, IMonitor>();
        
        public IMonitor GetMonitor(MonitorType type)
        {
            IMonitor monitor;
            lock (_sync) {
                if (_monitors == null) {
                    throw new ObjectDisposedException("MonitorFactory is disposed");
                }
                if (_monitors.ContainsKey(type)) {
                    monitor = _monitors[type];
                }
                else {
                    try {
                        monitor = MonitorBuilder.CreateMonitor(type);
                    }
                    catch (Exception ex) {
                        LOGGER.Error($"Failed creating monitor of type '{type}':", ex);
                        throw;
                    }

                    if (monitor != null) {
                        if (!monitor.IsSupported) {
                            LOGGER.Warn($"{monitor} is not supported");
                            return null;
                        }
                        monitor.Init();
                        _monitors.Add(type, monitor);
                    }
                }
            }
            return monitor;
        }

        public void Dispose()
        {
            if (_monitors != null) {
                lock (_sync) {
                    if (_monitors != null) {
                        foreach (var monitor in _monitors.Values) {
                            monitor.Dispose();
                        }
                        _monitors = null;
                    }
                }
            }
        }
    }
}
