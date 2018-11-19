package ru.nord.siwatch.backend.services.route.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoutePointDto {

    private Long id;

    private Long order;

    private Double latitude;

    private Double longitude;

    private Double altitude;

    private RoutePointInfoDto pointInfo;
}
