using Tizen.Location;

namespace SiWatchApp.Services
{
    public interface ILocationProvider
    {
        bool IsSupported { get; }
        Location GetCurrentLocation();
    }
}
