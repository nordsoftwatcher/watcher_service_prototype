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

        public override bool Start()
        {
            _systemMemoryUsage = new SystemMemoryUsage();
            _storage = StorageManager.Storages.FirstOrDefault(s => s.StorageType == StorageArea.Internal);
            return true;
        }

        public override void Dispose()
        {
            _systemMemoryUsage = null;
            _storage = null;
        }

        public override MonitorType MonitorType => MonitorType.Memory;

        public override MonitorValue GetCurrentValue()
        {
            if (_systemMemoryUsage == null) {
                throw GetInvalidAccessException();
            }

            _systemMemoryUsage.Update();
            var info = new MemoryInfo() {
                    FreeSystemMemory = _systemMemoryUsage.Free,
                    FreeStorageMemory = _storage?.AvaliableSpace
            };
            return new MonitorValue(info);
        }
    }
}
