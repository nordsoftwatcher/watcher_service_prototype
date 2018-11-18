package ru.nord.siwatch.backend.connectors.locationmonitoring.models;

import java.time.LocalDateTime;

public class Location
{
    private LocalDateTime deviceTime;
    private LocalDateTime recordTime;

    private double latitude;
    private double longitude;
    private double altitude;
    private double speed;
    private double direction;
    private double accuracy;
}
