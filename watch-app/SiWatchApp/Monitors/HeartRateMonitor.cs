namespace SiWatchApp.Monitors
{
    public class HeartRateMonitor : MonitorBase<int>
    {
        public override bool IsSupported => Tizen.Sensor.HeartRateMonitor.IsSupported;

        private Tizen.Sensor.HeartRateMonitor _sensor;

        public override void Init()
        {
            if (IsSupported) {
                if (_sensor == null) {
                    _sensor = new Tizen.Sensor.HeartRateMonitor();
                    _sensor.Start();
                }
            }
        }

        private static readonly string[] PRIVILEGES = { "http://tizen.org/privilege/healthinfo" };
        public override string[] Privileges => PRIVILEGES;

        public override MonitorType MonitorType => MonitorType.HeartRate;

        public override object GetCurrentValue()
        {
            return _sensor?.HeartRate;
        }

        public override void Dispose()
        {
            if (_sensor != null) {
                _sensor.Dispose();
                _sensor = null;
            }
        }
    }
}
