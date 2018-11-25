package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class LocationDto
{
    private double latitude;
    private double longitude;
    private double altitude;
    private double accuracy;
}
