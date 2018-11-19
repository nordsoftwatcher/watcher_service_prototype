package ru.nord.siwatch.backend.services.route.api.dto;

import java.util.Date;

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
