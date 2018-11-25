using SiWatchApp.Monitors;

namespace SiWatchApp.Models
{
    public class MonitorRecord : Record
    {
        public MonitorType MonitorType { get; set; }

        public MonitorRecord() { }

        public MonitorRecord(MonitorType monitorType, object monitorValue)
        {
            MonitorType = monitorType;
            Value = monitorValue;
        }
        
        public override string ToString()
        {
            return $"MonitorRecord{{MonitorType={MonitorType},Timestamp={Timestamp},Value={Value}}}";
        }
    }
}
