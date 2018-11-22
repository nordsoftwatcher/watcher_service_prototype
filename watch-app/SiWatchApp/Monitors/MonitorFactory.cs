using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SiWatchApp.Services;
using SiWatchApp.Utils;
using Xamarin.Forms.Internals;

namespace SiWatchApp.Monitors
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

        public async Task Init()
        {
            HashSet<string> privileges = new HashSet<string>();
            foreach (MonitorType mt in Enum.GetValues(typeof(MonitorType)).Cast<MonitorType>()) {
                var monitor = GetMonitor(mt);
                if (monitor.Privileges != null) {
                    privileges.UnionWith(monitor.Privileges);
                }
            }
            await _permissionManager.Demand(privileges);
        }

        public IMonitor GetMonitor(MonitorType type)
        {
            lock (_sync) {
                if (_monitors == null) {
                    throw new ObjectDisposedException("MonitorFactory is disposed");
                }

                IMonitor monitor = null;
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
                        throw ex;
                    }

                    if (monitor != null) {
                        _monitors.Add(type, monitor);
                    }
                }
                return monitor;
            }
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
