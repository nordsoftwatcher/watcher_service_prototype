using System;
using SiWatchApp.Models;
using Tizen.Network.Connection;

namespace SiWatchApp.Monitors
{
    public class NetworkMonitor : MonitorBase<NetworkInfo>
    {
        private static readonly string[] PRIVILEGES = { "http://tizen.org/privilege/location" };

        public override string[] Privileges => PRIVILEGES;

        public override object GetCurrentValue()
        {
            var con = ConnectionManager.CurrentConnection;
            return new NetworkInfo() { ConnectionState = con.State, ConnectionType = con.Type };
        }

        public override MonitorType MonitorType => MonitorType.Network;
    }
}
