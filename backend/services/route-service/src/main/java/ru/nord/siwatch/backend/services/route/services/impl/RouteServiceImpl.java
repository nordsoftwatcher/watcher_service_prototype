package ru.nord.siwatch.backend.services.route.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.services.route.api.model.CreateRouteInput;
import ru.nord.siwatch.backend.services.route.api.model.CreateRoutePointInput;
import ru.nord.siwatch.backend.services.route.entities.Route;
import ru.nord.siwatch.backend.services.route.entities.RoutePoint;
import ru.nord.siwatch.backend.services.route.enums.RouteStatus;
import ru.nord.siwatch.backend.services.route.mapping.RouteMapper;
import ru.nord.siwatch.backend.services.route.repositories.RoutePointRepository;
import ru.nord.siwatch.backend.services.route.repositories.RouteRepository;
import ru.nord.siwatch.backend.services.route.services.RouteService;

import java.util.ArrayList;
import java.util.List;

@Service("routeService")
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RoutePointRepository routePointRepository;

    @Autowired
    private RouteMapper routeMapper;

    @Override
    public Route createRoute(CreateRouteInput createRouteInput) {
        Route route = new Route();
        route.setStatus(RouteStatus.NOT_STARTED);
        routeRepository.save(route);
        /** Points */
        List<RoutePoint> points = new ArrayList<>(createRouteInput.getPoints().size());
        for (CreateRoutePointInput createRoutePointInput : createRouteInput.getPoints()) {
            RoutePoint point = routeMapper.toRoutePoint(createRoutePointInput);
            point.setRoute(route);
            points.add(point);
        }
        routePointRepository.saveAll(points);
        return route;
    }
}
