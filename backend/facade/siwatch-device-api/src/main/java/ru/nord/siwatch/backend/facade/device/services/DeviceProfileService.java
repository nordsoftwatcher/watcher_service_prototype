package ru.nord.siwatch.backend.facade.device.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.batterymonitoring.BatteryMonitoringConnector;
import ru.nord.siwatch.backend.connectors.batterymonitoring.models.BatteryLevelInfo;
import ru.nord.siwatch.backend.connectors.heartratemonitoring.HeartRateMonitoringConnector;
import ru.nord.siwatch.backend.connectors.heartratemonitoring.models.HeartRateInfo;
import ru.nord.siwatch.backend.connectors.locationmonitoring.LocationMonitoringConnector;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.LocationInfo;
import ru.nord.siwatch.backend.connectors.memorymonitoring.MemoryMonitoringConnector;
import ru.nord.siwatch.backend.connectors.memorymonitoring.models.MemoryInfo;
import ru.nord.siwatch.backend.connectors.networkmonitoring.NetworkMonitoringConnector;
import ru.nord.siwatch.backend.connectors.networkmonitoring.models.NetworkInfo;
import ru.nord.siwatch.backend.facade.device.models.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DeviceProfileService
{
    private final BatteryMonitoringConnector batteryMonitoringConnector;
    private final HeartRateMonitoringConnector heartRateMonitoringConnector;
    private final NetworkMonitoringConnector networkMonitoringConnector;
    private final MemoryMonitoringConnector memoryMonitoringConnector;
    private final LocationMonitoringConnector locationMonitoringConnector;

    public DeviceProfileService(
        BatteryMonitoringConnector batteryMonitoringConnector,
        HeartRateMonitoringConnector heartRateMonitoringConnector,
        NetworkMonitoringConnector networkMonitoringConnector,
        MemoryMonitoringConnector memoryMonitoringConnector,
        LocationMonitoringConnector locationMonitoringConnector)
    {
        this.batteryMonitoringConnector = batteryMonitoringConnector;
        this.heartRateMonitoringConnector = heartRateMonitoringConnector;
        this.networkMonitoringConnector = networkMonitoringConnector;
        this.memoryMonitoringConnector = memoryMonitoringConnector;
        this.locationMonitoringConnector = locationMonitoringConnector;
    }

    public Profile getDeviceProfile(String deviceId)
    {
        final MonitorConfig mc11 = new MonitorConfig();
        mc11.setType(MonitorType.Battery);
        mc11.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc12 = new MonitorConfig();
        mc12.setType(MonitorType.Location);
        mc12.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc13 = new MonitorConfig();
        mc13.setType(MonitorType.HeartRate);
        mc13.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc14 = new MonitorConfig();
        mc14.setType(MonitorType.Memory);
        mc14.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc15 = new MonitorConfig();
        mc15.setType(MonitorType.Network);
        mc15.setPollInterval(Duration.ofSeconds(5));

        final MonitoringPolicy mp1 = new MonitoringPolicy();
        mp1.setMinBatteryLevel(50);
        mp1.setSyncInterval(Duration.ofSeconds(10));
        mp1.setPacketSize(48);
        mp1.setMonitors(Arrays.asList(mc11, mc12, mc13, mc14, mc15));

        final MonitorConfig mc21 = new MonitorConfig();
        mc21.setType(MonitorType.Battery);
        mc21.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc22 = new MonitorConfig();
        mc22.setType(MonitorType.Location);
        mc22.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc23 = new MonitorConfig();
        mc23.setType(MonitorType.HeartRate);
        mc23.setPollInterval(Duration.ofSeconds(5));

        final MonitoringPolicy mp2 = new MonitoringPolicy();
        mp2.setMinBatteryLevel(5);
        mp2.setSyncInterval(Duration.ofSeconds(20));
        mp2.setPacketSize(48);
        mp2.setMonitors(Arrays.asList(mc21, mc22, mc23));

        final Profile profile = new Profile();
        profile.setMonitoring(Arrays.asList(mp1, mp2));

        return profile;
    }

    private static Long getLong(Map<String, Object> map, String key) {
        Long result = null;
        if(map.containsKey(key)) {
            Object value = map.get(key);
            if(value != null) {
                try {
                    result = Long.parseLong(value.toString());
                }
                catch (NumberFormatException ex) {
                    log.warn("Failed parsing '"+value+"' as long");
                }
            }
        }
        return result;
    }

    private static Double getDouble(Map<String, Object> map, String key) {
        Double result = null;
        if(map.containsKey(key)) {
            Object value = map.get(key);
            if(value != null) {
                try {
                    result = Double.parseDouble(value.toString());
                }
                catch (NumberFormatException ex) {
                    log.warn("Failed parsing '"+value+"' as double");
                }
            }
        }
        return result;
    }

    public void saveMonitors(String deviceId, List<MonitorRecord> records) {
        for(MonitorRecord record : records) {
            try {
                switch (record.getMonitorType()) {
                    case Battery:
                        float level = Float.parseFloat(record.getValue().toString());
                        BatteryLevelInfo batteryLevelInfo = new BatteryLevelInfo();
                        batteryLevelInfo.setDeviceId(deviceId);
                        batteryLevelInfo.setDeviceTime(record.getTimestamp());
                        batteryLevelInfo.setLevel(level);
                        batteryMonitoringConnector.save(batteryLevelInfo);
                        break;
                    case HeartRate:
                        float rate = Float.parseFloat(record.getValue().toString());
                        HeartRateInfo heartRateInfo = new HeartRateInfo();
                        heartRateInfo.setDeviceId(deviceId);
                        heartRateInfo.setDeviceTime(record.getTimestamp());
                        heartRateInfo.setRate(rate);
                        heartRateMonitoringConnector.save(heartRateInfo);
                        break;
                    case Memory:
                        Map<String, Object> mem = (Map<String, Object>) record.getValue();
                        MemoryInfo memoryInfo = new MemoryInfo();
                        memoryInfo.setDeviceId(deviceId);
                        memoryInfo.setDeviceTime(record.getTimestamp());
                        memoryInfo.setFreeSystemMemory(getLong(mem, "freeSystemMemory"));
                        memoryInfo.setFreeStorageMemory(getLong(mem, "freeStorageMemory"));
                        memoryMonitoringConnector.save(memoryInfo);
                        break;
                    case Network:
                        Map<String, Object> network = (Map<String, Object>) record.getValue();
                        NetworkInfo networkInfo = new NetworkInfo();
                        networkInfo.setDeviceId(deviceId);
                        networkInfo.setDeviceTime(record.getTimestamp());
                        networkInfo.setAvailable("Connected".equals(network.get("connectionState")));
                        networkMonitoringConnector.save(networkInfo);
                        break;
                    case Location:
                        Map<String, Object> loc = (Map<String, Object>) record.getValue();
                        LocationInfo locationInfo = new LocationInfo();
                        locationInfo.setDeviceId(deviceId);
                        locationInfo.setDeviceTime(record.getTimestamp());
                        locationInfo.setLatitude(getDouble(loc, "latitude"));
                        locationInfo.setLongitude(getDouble(loc, "longitude"));
                        locationInfo.setAltitude(getDouble(loc, "altitude"));
                        locationInfo.setAccuracy(getDouble(loc, "accuracy"));
                        locationMonitoringConnector.save(locationInfo);
                        break;
                }
            }
            catch (Exception ex) {
                log.error("Failed saving '"+record.getMonitorType()+"' monitor value", ex);
            }
        }
    }
}
