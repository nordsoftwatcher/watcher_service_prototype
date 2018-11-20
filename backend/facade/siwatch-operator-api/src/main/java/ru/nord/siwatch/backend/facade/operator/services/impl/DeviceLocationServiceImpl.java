package ru.nord.siwatch.backend.facade.operator.services.impl;

import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.locationmonitoring.LocationMonitoringConnector;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.facade.operator.services.DeviceLocationService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceLocationServiceImpl implements DeviceLocationService {

    private final LocationMonitoringConnector locationMonitoringConnector;

    public DeviceLocationServiceImpl(LocationMonitoringConnector locationMonitoringConnector) {
        this.locationMonitoringConnector = locationMonitoringConnector;
    }

    @Override
    public List<Location> queryDeviceLocation(String deviceId, LocalDateTime fromTime, LocalDateTime toTime) {
        return locationMonitoringConnector.find(deviceId, fromTime, toTime);
    }
}
