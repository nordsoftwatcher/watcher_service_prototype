using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SiWatchApp.Models;
using SiWatchApp.Monitors;
using SiWatchApp.Utils;
using Xamarin.Forms.Internals;

namespace SiWatchApp.Services
{
    public class MonitorFactory : IDisposable
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(MonitorFactory));

        private readonly object _sync = new object();
        private Dictionary<MonitorType, IMonitor> _monitors = new Dictionary<MonitorType, IMonitor>();
        private readonly PermissionManager _permissionManager;

        public MonitorFactory(PermissionManager permissionManager)
        {
            _permissionManager = permissionManager;
        }

        public async Task<IMonitor> GetMonitor(MonitorType type)
        {
            bool check = false;
            IMonitor monitor = null;
            lock (_sync)
            {
                if (_monitors == null) {
                    throw new ObjectDisposedException("MonitorFactory is disposed");
                }
                if (_monitors.ContainsKey(type)) {
                    monitor = _monitors[type];
                }
                else {
                    try {
                        switch (type) {
                            case MonitorType.Battery:
                                monitor = new BatteryLevelMonitor();
                                break;
                            case MonitorType.HeartRate:
                                monitor = new HeartRateMonitor();
                                break;
                            case MonitorType.Location:
                                monitor = new LocationMonitor();
                                break;
                            case MonitorType.Memory:
                                monitor = new MemoryMonitor();
                                break;
                            case MonitorType.Network:
                                monitor = new NetworkMonitor();
                                break;
                        }
                    }
                    catch (Exception ex) {
                        LOGGER.Error($"Failed creating monitor of type '{type}':", ex.ToString());
                        throw;
                    }

                    if (monitor != null) {
                        _monitors.Add(type, monitor);
                        check = true;
                    }
                }
            }
            if (check) {
                await _permissionManager.Demand(monitor.Privileges);
                if (!monitor.Start()) {
                    throw new ApplicationException($"{monitor} is not supported");
                }
            }
            return monitor;
        }

        public void Dispose()
        {
            lock (_sync) {
                _monitors.Values.ForEach(m => m.Dispose());
                _monitors = null;
            }
        }
    }
}
