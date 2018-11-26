package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
public class CheckPointResultDto extends AbstractDto {

    private Double radius;

    private Long order;

    private Double latitude;

    private Double longitude;

    private String name;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private LocalDateTime planArrivalTime;

    private LocalDateTime planDepartureTime;

    private String address;

    private String description;

}
