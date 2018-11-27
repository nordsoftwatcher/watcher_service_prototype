package ru.nord.siwatch.backend.facade.operator.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter @Setter @AllArgsConstructor
public class RouteDeviationDto {

    private Date startTime;

    private Date endTime;

    private Integer deviationMinutes;

}
