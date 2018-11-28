using System;
using System.Runtime.InteropServices;

namespace SiWatchApp.System
{
    public static class DevicePower
    {
        public static string Privilege = "http://tizen.org/privilege/display";

        private  const string DeviceLibrary = "libcapi-system-device.so.0";

        [DllImport(DeviceLibrary, EntryPoint = "device_power_request_lock", CallingConvention = CallingConvention.Cdecl)]
        private static extern int DevicePowerRequestLock(PowerLock type, int timeout_ms);
        [DllImport(DeviceLibrary, EntryPoint = "device_power_release_lock", CallingConvention = CallingConvention.Cdecl)]
        private static extern int DevicePowerReleaseLock(PowerLock type);

        public static int RequestLock(PowerLock powerLock, TimeSpan timeSpan)
        {
            return DevicePowerRequestLock(powerLock, (int) timeSpan.TotalMilliseconds);
        }

        public static int ReleaseLock(PowerLock powerLock)
        {
            return DevicePowerReleaseLock(powerLock);
        }
    }

    public enum PowerLock
    {
        Cpu = 0,
        DisplayNormal = 1,
        DisplayDim = 2,
    }
}
