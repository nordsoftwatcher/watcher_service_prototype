using System;

namespace SiWatchApp.Monitors
{
    public interface IMonitor : IDisposable
    {
        bool Start();
        MonitorValue GetCurrentValue();
        Type ValueType { get; }
        MonitorType MonitorType { get; }
        string[] Privileges { get; }
    }
}
