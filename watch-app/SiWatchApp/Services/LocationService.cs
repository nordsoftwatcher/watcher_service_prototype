using System;
using System.Threading;
using System.Threading.Tasks;
using SiWatchApp.Logging;
using Tizen.Location;

namespace SiWatchApp.Services
{
    public class LocationService : ILocationProvider, IDisposable
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(LocationService));

        public static readonly LocationService Instance = new LocationService();
        
        public bool IsSupported => LocatorHelper.IsSupportedType(_locator.LocationType);
        
        public ServiceState State { get; private set; } = ServiceState.Disabled;

        private Locator _locator;

        private LocationService(LocationType locationType = LocationType.Gps)
        {
            _locator = new Locator(locationType);
            _locator.ServiceStateChanged += LocatorOnServiceStateChanged;
        }

        public static string Privilege => "http://tizen.org/privilege/location";
        
        public void Dispose()
        {
            if (_locator != null) {
                _locator.Dispose();
                _locator = null;
            }
        }

        public void Start()
        {
            if (_locator == null) {
                throw new InvalidOperationException("Not initialized");
            }

            _locator.Start();
        }

        public void Stop()
        {
            if (_locator == null) {
                throw new InvalidOperationException("Not initialized");
            }

            _locator.Stop();
        }

        private void LocatorOnServiceStateChanged(object sender, ServiceStateChangedEventArgs e)
        {
            LOGGER.Debug(e.ServiceState);
            State = e.ServiceState;
        }

        public Location GetCurrentLocation()
        {
            try {
                return _locator != null && State == ServiceState.Enabled ? _locator.GetLocation() : null;
            }
            catch (Exception ex) {
                LOGGER.Warn("Failed getting current location", ex);
                return null;
            }
        }
    }
}
