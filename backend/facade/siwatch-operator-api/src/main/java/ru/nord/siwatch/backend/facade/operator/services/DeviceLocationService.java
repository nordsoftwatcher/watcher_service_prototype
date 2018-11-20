package ru.nord.siwatch.backend.facade.operator.services;

import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceLocationService {

    List<Location> queryDeviceLocation(String deviceId, LocalDateTime fromTime, LocalDateTime toTime);

}
