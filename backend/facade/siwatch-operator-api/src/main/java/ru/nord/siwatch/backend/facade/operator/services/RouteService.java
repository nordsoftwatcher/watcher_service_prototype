package ru.nord.siwatch.backend.facade.operator.services;

import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.CreateRouteInput;

public interface RouteService {

    Route getRouteById(Long routeId);

    Route createRoute(CreateRouteInput createRouteInput);
}
