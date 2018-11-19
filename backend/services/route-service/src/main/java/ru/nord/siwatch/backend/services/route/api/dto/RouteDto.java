package ru.nord.siwatch.backend.services.route.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.route.enums.RouteStatus;
import java.util.List;

@Getter @Setter
public class RouteDto {

    private Long id;

    private RouteStatus status;

    private List<RoutePointDto> routePoints;

    private List<RoutePointDto> checkPoints;

}
