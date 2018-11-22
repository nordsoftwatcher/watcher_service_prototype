package ru.nord.siwatch.backend.connectors.locationmonitoring.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Location {

    private Long id;

    private LocalDateTime deviceTime;
    private LocalDateTime recordTime;

    private double latitude;
    private double longitude;
    private Double altitude;
    private Double speed;
    private Double direction;
    private Double accuracy;
}
