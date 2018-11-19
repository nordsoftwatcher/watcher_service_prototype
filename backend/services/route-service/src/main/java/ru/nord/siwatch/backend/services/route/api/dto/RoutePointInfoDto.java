package ru.nord.siwatch.backend.services.route.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoutePointInfoDto {

    private String name;

    private Date arrivalTime;

    private Date departureTime;

    private Integer planTime;

    private Integer factTime;

    private String address;

    private String description;

    private Boolean passed;
}
