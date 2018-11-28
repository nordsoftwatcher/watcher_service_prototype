package ru.nord.siwatch.backend.facade.operator.reports.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class RouteIntervalDto {

    private String startName;

    private String endName;

    private Date startTime;

    private Date endTime;

    private Date factStartTime;

    private Date factEndTime;

    private Integer planTimeMinutes;

    private Integer factTimeMinutes;

    private Boolean arrivalLate;

    private Boolean departureLate;

}
