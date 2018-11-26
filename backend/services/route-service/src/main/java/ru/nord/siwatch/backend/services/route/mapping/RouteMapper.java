package ru.nord.siwatch.backend.services.route.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nord.siwatch.backend.services.route.api.dto.RouteDto;
import ru.nord.siwatch.backend.services.route.api.dto.RoutePointDto;
import ru.nord.siwatch.backend.services.route.api.model.CreateCheckPointInput;
import ru.nord.siwatch.backend.services.route.api.model.CreateRouteInput;
import ru.nord.siwatch.backend.services.route.api.model.CreateRoutePointInput;
import ru.nord.siwatch.backend.services.route.entities.CheckPoint;
import ru.nord.siwatch.backend.services.route.entities.Route;
import ru.nord.siwatch.backend.services.route.entities.RoutePoint;

import java.util.List;

@Mapper
public interface RouteMapper {

    @Mapping(ignore = true, target = "routePoints")
    @Mapping(ignore = true, target = "checkPoints")
    Route toRoute(CreateRouteInput createRouteInput);

    RouteDto toRouteDto(Route route);

    List<RouteDto> toRouteDtoList(List<Route> routes);

    RoutePointDto toRoutePointDto(RoutePoint routePoint);

    List<RoutePointDto> toRoutePointDtoList(List<RoutePoint> routePoints);

    RoutePoint toRoutePoint(CreateRoutePointInput createRoutePointInput);

    CheckPoint toCheckPoint(CreateCheckPointInput createCheckPointInput);
}
