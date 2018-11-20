package ru.nord.siwatch.backend.facade.operator.services;

import ru.nord.siwatch.backend.connectors.route.models.Route;

public interface RouteService {

    Route getRouteById(Long routeId);
}
