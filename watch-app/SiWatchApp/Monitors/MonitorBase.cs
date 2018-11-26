using System;

namespace SiWatchApp.Monitors
{
    public abstract class MonitorBase<T> : IMonitor
    {
        public Type ValueType => typeof(T);

        public virtual void Dispose() { }

        public virtual void Init() { }

        public virtual bool IsSupported => true;

        public abstract object GetCurrentValue();
        
        public abstract MonitorType MonitorType { get; }

        public virtual string[] Privileges => null;

        public override string ToString()
        {
            return GetType().Name;
        }
    }
}
