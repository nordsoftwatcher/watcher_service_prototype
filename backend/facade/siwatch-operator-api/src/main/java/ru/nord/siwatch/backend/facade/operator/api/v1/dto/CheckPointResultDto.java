package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class CheckPointResultDto extends AbstractDto {

    private Double radius;

    private Long order;

    private Double latitude;

    private Double longitude;

    private String name;

    private LocalDateTime factArrivalTime;

    private LocalDateTime factDepartureTime;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private String address;

    private String description;

}
