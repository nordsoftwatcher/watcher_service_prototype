package ru.nord.siwatch.backend.connectors.route.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
public class RoutePointInfo {

    private String name;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    private Integer planTime;

    private Integer factTime;

    private String address;

    private String description;

    private Boolean passed;

}
