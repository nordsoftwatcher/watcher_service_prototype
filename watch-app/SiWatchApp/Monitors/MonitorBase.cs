using System;

namespace SiWatchApp.Monitors
{
    public abstract class MonitorBase<T> : IMonitor
    {
        public virtual void Dispose() { }
    
        public virtual bool Start() => true;

        public abstract MonitorValue GetCurrentValue();

        public Type ValueType => typeof(T);

        public abstract MonitorType MonitorType { get; }

        public virtual string[] Privileges => null;

        public override string ToString()
        {
            return this.GetType().Name;
        }

        protected Exception GetInvalidAccessException()
        {
            return new InvalidOperationException($"{GetType().Name} is disposed or not supported");
        }
    }
}
