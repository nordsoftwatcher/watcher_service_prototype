using System;
using System.Threading;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Services;
using SiWatchApp.Utils;
using Tizen.Location;

namespace SiWatchApp.Monitors
{
    public class LocationMonitor : MonitorBase<LocationInfo>
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(LocationMonitor));
        
        private readonly ILocationProvider _locationProvider;

        public override bool IsSupported => _locationProvider?.IsSupported ?? false;

        public LocationMonitor(ILocationProvider locationProvider)
        {
            _locationProvider = locationProvider;
        }
        
        public override MonitorType MonitorType => MonitorType.Location;

        public override object GetCurrentValue()
        {
            var location = _locationProvider?.GetCurrentLocation();
            return location != null ? new LocationInfo(location) : null;
        }
    }
}
