package ru.nord.siwatch.backend.connectors.route.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RouteInfo {

    private String name;

    private Long supervisorId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<RoutePointInfo> routePoints;

    private List<CheckPointInfo> checkPoints;
}
