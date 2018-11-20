package ru.nord.siwatch.backend.facade.operator.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.route.RouteConnector;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;

@Service("routeService")
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteConnector routeConnector;

    @Override
    public Route getRouteById(Long routeId) {
        return routeConnector.getRoute(routeId);
    }
}
