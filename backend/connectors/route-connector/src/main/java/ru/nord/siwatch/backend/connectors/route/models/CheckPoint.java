package ru.nord.siwatch.backend.connectors.route.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CheckPoint {

    private Long id;

    private Double radius;

    private Long order;

    private Double latitude;

    private Double longitude;

    private String name;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private Integer planTime;

    private Integer factTime;

    private String address;

    private String description;

}
