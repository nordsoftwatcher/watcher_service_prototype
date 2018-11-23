package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
public class ArrivalDepartureInfo {

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

}
