using System;
using SiWatchApp.Models;

namespace SiWatchApp.Monitors
{
    public interface IMonitor : IRequirePrivileges, IDisposable
    {
        bool IsSupported { get; }
        void Init();
        object GetCurrentValue();
        Type ValueType { get;  }
        MonitorType MonitorType { get; }
    }
}
