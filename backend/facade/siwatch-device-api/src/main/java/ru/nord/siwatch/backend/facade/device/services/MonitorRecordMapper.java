package ru.nord.siwatch.backend.facade.device.services;

import org.springframework.stereotype.Component;
import ru.nord.siwatch.backend.connectors.batterymonitoring.models.BatteryLevelInfo;
import ru.nord.siwatch.backend.connectors.heartratemonitoring.models.HeartRateInfo;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.LocationInfo;
import ru.nord.siwatch.backend.connectors.memorymonitoring.models.MemoryInfo;
import ru.nord.siwatch.backend.connectors.networkmonitoring.models.NetworkInfo;
import ru.nord.siwatch.backend.facade.device.models.monitoring.MonitorRecord;
import ru.nord.siwatch.backend.facade.device.utils.MapHelper;

import java.util.Map;

@Component
public class MonitorRecordMapper
{
    public Object getMonitorValueInfo(String deviceId, MonitorRecord record)
    {
        switch (record.getMonitorType()) {
            case Battery:
                float level = Float.parseFloat(record.getValue().toString());
                BatteryLevelInfo batteryLevelInfo = new BatteryLevelInfo();
                batteryLevelInfo.setDeviceId(deviceId);
                batteryLevelInfo.setDeviceTime(record.getTimestamp());
                batteryLevelInfo.setLevel(level);
                return batteryLevelInfo;
            case HeartRate:
                float rate = Float.parseFloat(record.getValue().toString());
                HeartRateInfo heartRateInfo = new HeartRateInfo();
                heartRateInfo.setDeviceId(deviceId);
                heartRateInfo.setDeviceTime(record.getTimestamp());
                heartRateInfo.setRate(rate);
                return heartRateInfo;
            case Memory:
                Map<String, Object> mem = (Map<String, Object>) record.getValue();
                MemoryInfo memoryInfo = new MemoryInfo();
                memoryInfo.setDeviceId(deviceId);
                memoryInfo.setDeviceTime(record.getTimestamp());
                memoryInfo.setFreeSystemMemory(MapHelper.getLong(mem, "freeSystemMemory"));
                memoryInfo.setFreeStorageMemory(MapHelper.getLong(mem, "freeStorageMemory"));
                return memoryInfo;
            case Network:
                Map<String, Object> network = (Map<String, Object>) record.getValue();
                NetworkInfo networkInfo = new NetworkInfo();
                networkInfo.setDeviceId(deviceId);
                networkInfo.setDeviceTime(record.getTimestamp());
                networkInfo.setAvailable("Connected".equals(network.get("connectionState")));
                return networkInfo;
            case Location:
                Map<String, Object> loc = (Map<String, Object>) record.getValue();
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setDeviceId(deviceId);
                locationInfo.setDeviceTime(record.getTimestamp());
                locationInfo.setLatitude(MapHelper.getDouble(loc, "latitude"));
                locationInfo.setLongitude(MapHelper.getDouble(loc, "longitude"));
                locationInfo.setAltitude(MapHelper.getDouble(loc, "altitude"));
                locationInfo.setAccuracy(MapHelper.getDouble(loc, "accuracy"));
                return locationInfo;
        }
        return null;
    }
}
