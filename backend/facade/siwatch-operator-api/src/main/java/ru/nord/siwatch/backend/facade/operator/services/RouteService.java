package ru.nord.siwatch.backend.facade.operator.services;

import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.CheckPointResultDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.CreateRouteInput;

import java.util.List;

public interface RouteService {

    Route getRouteById(Long routeId);

    Route createRoute(CreateRouteInput createRouteInput);

    List<LocationDto> transformLocations(List<Location> locations, Route route);

    List<CheckPointResultDto> transformCheckpoints(List<Location> locations, Route route);
}
