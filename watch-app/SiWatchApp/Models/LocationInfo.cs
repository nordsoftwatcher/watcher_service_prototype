using Tizen.Location;

namespace SiWatchApp.Models
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
}
