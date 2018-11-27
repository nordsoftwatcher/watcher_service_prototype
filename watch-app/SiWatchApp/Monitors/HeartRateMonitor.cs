using System;
using System.Threading;
using Tizen.Sensor;

namespace SiWatchApp.Monitors
{
    public class HeartRateMonitor : MonitorBase<int>
    {
        public override bool IsSupported => Tizen.Sensor.HeartRateMonitor.IsSupported;

        private Tizen.Sensor.HeartRateMonitor _sensor;

        private volatile int _heartRate = 0;

        public override void Init()
        {
            if (IsSupported) {
                if (_sensor == null) {
                    _sensor = new Tizen.Sensor.HeartRateMonitor();
                    _sensor.PausePolicy = SensorPausePolicy.None;
                    _sensor.Interval = 100;
                    //_sensor.TimeSpan = TimeSpan.FromMilliseconds(100);
                    _sensor.DataUpdated += OnSensorDataUpdated;
                    _sensor.Start();
                }
            }
        }

        private void OnSensorDataUpdated(object sender, HeartRateMonitorDataUpdatedEventArgs e)
        {
            _heartRate = e.HeartRate;
        }

        private static readonly string[] PRIVILEGES = { "http://tizen.org/privilege/healthinfo" };
        public override string[] Privileges => PRIVILEGES;

        public override MonitorType MonitorType => MonitorType.HeartRate;

        public override object GetCurrentValue()
        {
            return _heartRate;
            //return _sensor?.HeartRate;
        }

        public override void Dispose()
        {
            if (_sensor != null) {
                _sensor.DataUpdated -= OnSensorDataUpdated;
                _sensor.Dispose();
                _sensor = null;
            }
        }
    }
}
