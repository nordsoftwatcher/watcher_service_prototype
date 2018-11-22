package ru.nord.siwatch.backend.facade.device.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.facade.device.models.profile.MonitorConfig;
import ru.nord.siwatch.backend.facade.device.models.profile.MonitorType;
import ru.nord.siwatch.backend.facade.device.models.profile.MonitoringPolicy;
import ru.nord.siwatch.backend.facade.device.models.profile.Profile;

import java.time.Duration;
import java.util.Arrays;

@Service
public class DeviceProfileService
{
    public Profile getDeviceProfile(String deviceId)
    {
        final MonitorConfig mc1 = new MonitorConfig();
        mc1.setType(MonitorType.Battery);
        mc1.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc2 = new MonitorConfig();
        mc2.setType(MonitorType.Location);
        mc2.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc3 = new MonitorConfig();
        mc3.setType(MonitorType.HeartRate);
        mc3.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc4 = new MonitorConfig();
        mc4.setType(MonitorType.Memory);
        mc4.setPollInterval(Duration.ofSeconds(5));
        final MonitorConfig mc5 = new MonitorConfig();
        mc5.setType(MonitorType.Network);
        mc5.setPollInterval(Duration.ofSeconds(5));

        final MonitoringPolicy mp = new MonitoringPolicy();
        mp.setMinBatterLevel(10);
        mp.setFlushInterval(Duration.ofSeconds(15));
        mp.setPacketSize(4);
        mp.setMonitors(Arrays.asList(mc1, mc2, mc3, mc4, mc5));

        final Profile profile = new Profile();
        profile.setMonitoring(Arrays.asList(mp));

        return profile;
    }
}
