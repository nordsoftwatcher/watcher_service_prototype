package ru.nord.siwatch.backend.connectors.locationmonitoring.models;

import java.time.LocalDateTime;

public class Location
{
    private LocalDateTime deviceTime;
    private LocalDateTime recordTime;

    private double latitude;
    private double longitude;
    private Double altitude;
    private Double speed;
    private Double direction;
    private Double accuracy;
}
