package ru.nord.siwatch.backend.services.route.services;

import ru.nord.siwatch.backend.services.route.api.model.CreateRouteInput;
import ru.nord.siwatch.backend.services.route.entities.Route;

public interface RouteService {

    Route createRoute(CreateRouteInput createRouteInput);
}
