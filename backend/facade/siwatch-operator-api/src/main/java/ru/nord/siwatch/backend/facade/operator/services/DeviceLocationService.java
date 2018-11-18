package ru.nord.siwatch.backend.facade.operator.services;

import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.locationmonitoring.LocationMonitoringConnector;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceLocationService
{
    private final LocationMonitoringConnector locationMonitoringConnector;

    public DeviceLocationService(LocationMonitoringConnector locationMonitoringConnector)
    {
        this.locationMonitoringConnector = locationMonitoringConnector;
    }

    public List<Location> queryDeviceLocation(String deviceId, LocalDateTime fromTime, LocalDateTime toTime)
    {
        return locationMonitoringConnector.find(deviceId, fromTime, toTime);
    }
}
