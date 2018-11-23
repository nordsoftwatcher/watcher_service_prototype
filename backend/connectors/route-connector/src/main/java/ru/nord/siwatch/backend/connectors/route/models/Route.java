package ru.nord.siwatch.backend.connectors.route.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Route {

    private Long id;

    private String name;

    private Long supervisorId;

    private String status;

    private List<RoutePoint> routePoints;

    private List<CheckPoint> checkPoints;

}