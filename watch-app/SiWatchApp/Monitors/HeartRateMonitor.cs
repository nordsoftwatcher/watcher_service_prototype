using System;
using ElmSharp;

namespace SiWatchApp.Monitors
{
    public class HeartRateMonitor : MonitorBase<int>
    {
        private Tizen.Sensor.HeartRateMonitor _sensor;

        public override bool Start()
        {
            if (!Tizen.Sensor.HeartRateMonitor.IsSupported) {
                return false;
            }

            if (_sensor == null) {
                _sensor = new Tizen.Sensor.HeartRateMonitor();
                _sensor.Start();
            }

            return true;
        }

        private static readonly string[] PRIVILEGES = { "http://tizen.org/privilege/healthinfo" };
        public override string[] Privileges => PRIVILEGES;

        public override MonitorType MonitorType => MonitorType.HeartRate;

        public override MonitorValue GetCurrentValue()
        {
            if (_sensor == null) {
                throw GetInvalidAccessException();
            }
            return new MonitorValue(_sensor.HeartRate);
        }

        public override void Dispose()
        {
            if (_sensor != null) {
                _sensor.Stop();
                _sensor.Dispose();
                _sensor = null;
            }
        }
    }
}
