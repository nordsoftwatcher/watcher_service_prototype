using System;
using System.Threading;
using SiWatchApp.Models;
using SiWatchApp.Services;
using SiWatchApp.Utils;
using Tizen.Location;

namespace SiWatchApp.Monitors
{
    public class LocationInfo : PositionInfo
    {
        public double Accuracy { get; set; }

        public LocationInfo(Location location)
        {
            Latitude = location.Latitude;
            Longitude = location.Longitude;
            Altitude = location.Altitude;
            Accuracy = location.Accuracy;
        }

        public override string ToString()
        {
            return $"LocationInfo{{Latitude={Latitude},Longitude={Longitude},Altitude={Altitude},Accuracy={Accuracy}}}";
        }
    }

    public class LocationMonitor : MonitorBase<LocationInfo>
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(LocationMonitor));

        private Locator _locator;
        private volatile bool _active;

        private static readonly string[] PRIVILEGES = { "http://tizen.org/privilege/location" };
        public override string[] Privileges => PRIVILEGES;

        public override bool Start()
        {
            if (!LocatorHelper.IsSupportedType(LocationType.Gps)) {
                return false;
            }

            if (_locator == null) {
                _locator = new Locator(LocationType.Gps);
                _locator.ServiceStateChanged += HandleStateChanged;
                _locator.Start();
            }

            return true;
        }

        private void HandleStateChanged(object sender, ServiceStateChangedEventArgs e)
        {
            LOGGER.Debug(e.ServiceState);
            _active = e.ServiceState == ServiceState.Enabled;
        }

        public override void Dispose()
        {
            if (_locator != null) {
                _locator.Dispose();
                _locator = null;
            }
        }
        
        public override MonitorType MonitorType => MonitorType.Location;

        public override MonitorValue GetCurrentValue()
        {
            if (_locator == null) {
                throw GetInvalidAccessException();
            }

            Location location = _active ? _locator.GetLocation() : null;
            return location != null ? new MonitorValue(new LocationInfo(location)) : null;
        }
    }
}
