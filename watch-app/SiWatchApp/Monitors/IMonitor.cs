using System;
using SiWatchApp.Models;

namespace SiWatchApp.Monitors
{
    public interface IMonitor : IRequirePrivileges, IDisposable
    {
        bool Start();
        MonitorValue GetCurrentValue();
        Type ValueType { get; }
        MonitorType MonitorType { get; }
    }
}
