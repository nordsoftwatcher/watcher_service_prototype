package ru.nord.siwatch.backend.connectors.route.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoutePoint {

    private Long id;

    private Long order;

    private Double latitude;

    private Double longitude;

    private Double altitude;

    private RoutePointInfo pointInfo;

}
