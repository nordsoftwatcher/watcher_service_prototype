package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class CheckPointResultDto extends AbstractDto {

    private Double radius;

    private Long order;

    private Double latitude;

    private Double longitude;

    private String name;

    private Date arrivalTime;

    private Date departureTime;

    private Integer planTime;

    private Integer factTime;

    private String address;

    private String description;

}
