using SiWatchApp.Monitors;

namespace SiWatchApp.Services
{
    public static class MonitorBuilder
    {
        public static IMonitor CreateMonitor(MonitorType type)
        {
            IMonitor monitor = null;
            switch (type)
            {
                case MonitorType.Battery:
                    monitor = new BatteryLevelMonitor();
                    break;
                case MonitorType.HeartRate:
                    monitor = new HeartRateMonitor();
                    break;
                case MonitorType.Location:
                    monitor = new LocationMonitor(LocationService.Instance);
                    break;
                case MonitorType.Memory:
                    monitor = new MemoryMonitor();
                    break;
                case MonitorType.Network:
                    monitor = new NetworkMonitor();
                    break;
            }

            return monitor;
        }
    }
}
