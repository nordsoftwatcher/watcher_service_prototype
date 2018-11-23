using Tizen.Network.Connection;

namespace SiWatchApp.Models {
    public struct NetworkInfo
    {
        public ConnectionState ConnectionState { get; set; }
        public ConnectionType ConnectionType { get; set; }

        public override string ToString()
        {
            return $"NetworkInfo{{ConnectionState={ConnectionState},ConnectionType={ConnectionType}}}";
        }
    }
}