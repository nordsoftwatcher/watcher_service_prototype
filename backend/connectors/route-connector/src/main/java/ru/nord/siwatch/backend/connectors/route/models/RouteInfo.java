package ru.nord.siwatch.backend.connectors.route.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RouteInfo {

    private Long supervisorId;

    private List<RoutePointInfo> points;

    private List<CheckPointInfo> checkPoints;
}
