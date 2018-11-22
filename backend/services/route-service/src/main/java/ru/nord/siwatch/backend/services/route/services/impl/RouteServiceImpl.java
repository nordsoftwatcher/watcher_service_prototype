package ru.nord.siwatch.backend.services.route.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.services.route.api.model.CreateCheckPointInput;
import ru.nord.siwatch.backend.services.route.api.model.CreateRouteInput;
import ru.nord.siwatch.backend.services.route.api.model.CreateRoutePointInput;
import ru.nord.siwatch.backend.services.route.entities.CheckPoint;
import ru.nord.siwatch.backend.services.route.entities.Route;
import ru.nord.siwatch.backend.services.route.entities.RoutePoint;
import ru.nord.siwatch.backend.services.route.enums.RouteStatus;
import ru.nord.siwatch.backend.services.route.mapping.RouteMapper;
import ru.nord.siwatch.backend.services.route.repositories.CheckPointRepository;
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
    private CheckPointRepository checkPointRepository;

    @Autowired
    private RouteMapper routeMapper;

    @Override
    public Route createRoute(CreateRouteInput createRouteInput) {
        Route route = new Route();
        route.setSupervisorId(createRouteInput.getSupervisorId());
        route.setStatus(RouteStatus.NOT_STARTED);
        routeRepository.save(route);
        /** Points */
        List<RoutePoint> points = new ArrayList<>(createRouteInput.getRoutePoints().size());
        for (CreateRoutePointInput createRoutePointInput : createRouteInput.getRoutePoints()) {
            RoutePoint point = routeMapper.toRoutePoint(createRoutePointInput);
            point.setRoute(route);
            points.add(point);
        }
        routePointRepository.saveAll(points);
        /** Check points */
        List<CheckPoint> checkPoints = new ArrayList<>(createRouteInput.getCheckPoints().size());
        for (CreateCheckPointInput createCheckPointInput : createRouteInput.getCheckPoints()) {
            CheckPoint checkPoint = routeMapper.toCheckPoint(createCheckPointInput);
            checkPoint.setRoute(route);
            checkPoints.add(checkPoint);
        }
        checkPointRepository.saveAll(checkPoints);
        return route;
    }
}
