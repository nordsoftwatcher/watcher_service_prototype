using SiWatchApp.Monitors;

namespace SiWatchApp.Models
{
    public class MonitoringRecord : DataRecord
    {
        public MonitorType MonitorType { get; }

        public MonitoringRecord(MonitorType monitorType, MonitorValue monitorValue)
        {
            MonitorType = monitorType;
            Value = monitorValue;
        }

        public override string ToString()
        {
            return $"MonitoringRecord{{MonitorType={MonitorType}, Value={Value}}}";
        }
    }
}
