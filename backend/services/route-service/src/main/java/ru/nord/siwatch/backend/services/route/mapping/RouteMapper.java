package ru.nord.siwatch.backend.services.route.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nord.siwatch.backend.services.route.api.dto.RouteDto;
import ru.nord.siwatch.backend.services.route.api.dto.RoutePointDto;
import ru.nord.siwatch.backend.services.route.api.dto.RoutePointInfoDto;
import ru.nord.siwatch.backend.services.route.entities.Route;
import ru.nord.siwatch.backend.services.route.entities.RoutePoint;
import ru.nord.siwatch.backend.services.route.entities.RoutePointInfo;

import java.util.List;

@Mapper
public interface RouteMapper {

    @Mapping(target = "routePoints", ignore = true)
    RouteDto toRouteDto(Route route);

    List<RouteDto> toRouteDtoList(List<Route> routes);

    RoutePointDto toRoutePointDto(RoutePoint routePoint);

    List<RoutePointDto> toRoutePointDtoList(List<RoutePoint> routePoints);

    RoutePointInfoDto map(RoutePointInfo value);
}
