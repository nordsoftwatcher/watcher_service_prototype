package ru.nord.siwatch.backend.connectors.batterymonitoring.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BatteryLevel {

    private Long id;
    private String deviceId;
    private LocalDateTime deviceTime;
    private float level;

}
