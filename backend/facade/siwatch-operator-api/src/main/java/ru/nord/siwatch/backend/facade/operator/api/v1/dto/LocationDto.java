package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LocationDto extends AbstractDto {

    private LocalDateTime deviceTime;
    private LocalDateTime recordTime;

    private double latitude;
    private double longitude;
    private Double altitude;
    private Double speed;
    private Double direction;
    private Double accuracy;

    private Double routeDistance;

}
