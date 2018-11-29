package ru.nord.siwatch.backend.facade.operator.reports.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class CheckPointPassedDto {

    private String name;

    private String address;

    private String description;

    private Date arrivalTime;

    private Date departureTime;

    private Date factArrivalTime;

    private Date factDepartureTime;

    private Boolean arrivalLate;

    private Boolean departureLate;

    private Integer planTime;

    private Integer factTime;

    private Boolean passed;

}
