package ru.nord.siwatch.backend.facade.operator.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.route.RouteConnector;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.route.models.RouteInfo;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.CreateRouteInput;
import ru.nord.siwatch.backend.facade.operator.mapping.OperatorMapper;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;

@Service("routeService")
@Slf4j
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteConnector routeConnector;

    @Autowired
    private OperatorMapper operatorMapper;

    @Override
    public Route getRouteById(Long routeId) {
        return routeConnector.getRoute(routeId);
    }

    @Override
    public Route createRoute(CreateRouteInput createRouteInput) {
        RouteInfo routeInfo = operatorMapper.toRouteInfo(createRouteInput);
        return routeConnector.save(routeInfo);
    }
}
