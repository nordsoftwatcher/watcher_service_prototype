using System;
using System.Linq;
using System.Runtime.Serialization;
using SiWatchApp.Models;
using Tizen.System;

namespace SiWatchApp.Monitors
{
    public class MemoryMonitor : MonitorBase<MemoryInfo>
    {
        private SystemMemoryUsage _systemMemoryUsage;
        private Storage _storage;

        private static readonly string[] PRIVILEGES = { "http://tizen.org/privilege/systemmonitor" };
        public override string[] Privileges => PRIVILEGES;

        public override void Init()
        {
            _systemMemoryUsage = new SystemMemoryUsage();
            _storage = StorageManager.Storages.FirstOrDefault(s => s.StorageType == StorageArea.Internal);
        }

        public override void Dispose()
        {
            _systemMemoryUsage = null;
            _storage = null;
        }

        public override MonitorType MonitorType => MonitorType.Memory;

        public override object GetCurrentValue()
        {
            if (_systemMemoryUsage != null) {
                _systemMemoryUsage.Update();
                return new MemoryInfo() {
                        FreeSystemMemory = (ulong) _systemMemoryUsage.Free*1024,
                        FreeStorageMemory = _storage?.AvaliableSpace
                };
            }
            return null;
        }
    }
}
