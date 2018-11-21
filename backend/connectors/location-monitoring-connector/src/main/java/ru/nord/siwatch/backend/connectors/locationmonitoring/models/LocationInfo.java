package ru.nord.siwatch.backend.connectors.locationmonitoring.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LocationInfo {
    private String deviceId;
    private LocalDateTime deviceTime;
    private double latitude;
    private double longitude;
    private double altitude;
    private double speed;
    private double direction;
    private double accuracy;
}
