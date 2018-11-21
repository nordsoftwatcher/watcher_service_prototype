package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.connectors.route.models.CheckPoint;
import ru.nord.siwatch.backend.connectors.route.models.RoutePoint;

import java.util.List;

@Getter @Setter
public class RouteDto extends AbstractDto {

    private SupervisorDto supervisor;

    private String status;

    private List<RoutePoint> routePoints;

    private List<CheckPoint> checkPoints;

}
