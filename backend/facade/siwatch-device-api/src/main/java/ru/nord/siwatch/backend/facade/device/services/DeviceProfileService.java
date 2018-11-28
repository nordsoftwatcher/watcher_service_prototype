package ru.nord.siwatch.backend.facade.device.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.facade.device.models.monitoring.MonitorType;
import ru.nord.siwatch.backend.facade.device.models.profile.MonitorConfig;
import ru.nord.siwatch.backend.facade.device.models.profile.MonitoringPolicy;
import ru.nord.siwatch.backend.facade.device.models.profile.Profile;

import java.time.Duration;
import java.util.Arrays;

@Service
@Slf4j
public class DeviceProfileService
{
    public DeviceProfileService() { }

    public Profile getDeviceProfile(String deviceId)
    {
        final MonitorConfig mc11 = new MonitorConfig();
        mc11.setType(MonitorType.Battery);
        mc11.setPollInterval(Duration.ofSeconds(30));
        final MonitorConfig mc12 = new MonitorConfig();
        mc12.setType(MonitorType.Location);
        mc12.setPollInterval(Duration.ofSeconds(4));
        final MonitorConfig mc13 = new MonitorConfig();
        mc13.setType(MonitorType.HeartRate);
        mc13.setPollInterval(Duration.ofSeconds(4));
        final MonitorConfig mc14 = new MonitorConfig();
        mc14.setType(MonitorType.Memory);
        mc14.setPollInterval(Duration.ofSeconds(30));
        final MonitorConfig mc15 = new MonitorConfig();
        mc15.setType(MonitorType.Network);
        mc15.setPollInterval(Duration.ofSeconds(30));

        final MonitoringPolicy mp1 = new MonitoringPolicy();
        mp1.setMinBatteryLevel(30);
        mp1.setSyncInterval(Duration.ofSeconds(8));
        mp1.setPacketSize(1000);
        mp1.setMonitors(Arrays.asList(mc11, mc12, mc13, mc14, mc15));

        final MonitorConfig mc21 = new MonitorConfig();
        mc21.setType(MonitorType.Battery);
        mc21.setPollInterval(Duration.ofSeconds(60));
        final MonitorConfig mc22 = new MonitorConfig();
        mc22.setType(MonitorType.Location);
        mc22.setPollInterval(Duration.ofSeconds(6));
        final MonitorConfig mc23 = new MonitorConfig();
        mc23.setType(MonitorType.HeartRate);
        mc23.setPollInterval(Duration.ofSeconds(6));

        final MonitoringPolicy mp2 = new MonitoringPolicy();
        mp2.setMinBatteryLevel(5);
        mp2.setSyncInterval(Duration.ofSeconds(25));
        mp2.setPacketSize(400);
        mp2.setMonitors(Arrays.asList(mc21, mc22, mc23));

        final Profile profile = new Profile();
        profile.setMonitoring(Arrays.asList(mp1, mp2));

        return profile;
    }
}
