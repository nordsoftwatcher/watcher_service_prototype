package ru.nord.siwatch.backend.facade.device.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Location
{
    private double latitude;
    private double longitude;
    private double altitude;
    private double accuracy;
}

