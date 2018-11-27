package ru.nord.siwatch.backend.facade.operator.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.RouteConnector;
import ru.nord.siwatch.backend.connectors.route.models.CheckPoint;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.route.models.RouteInfo;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.ArrivalDepartureInfo;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.CheckPointResultDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.CreateRouteInput;
import ru.nord.siwatch.backend.facade.operator.mapping.OperatorMapper;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;
import ru.nord.siwatch.backend.facade.operator.utils.OperatorLocationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Override
    public List<LocationDto> transformLocations(List<Location> locations, Route route) {
        if (CollectionUtils.isEmpty(locations)) {
            return Collections.emptyList();
        }
        List<LocationDto> result = new ArrayList<>(locations.size());
        for (Location location : locations) {
            LocationDto tempLocation = operatorMapper.toLocationDto(location);
            tempLocation.setRouteDistance(OperatorLocationUtils.distanceFromRoute(route, location));
            result.add(tempLocation);
        }
        return result;
    }

    @Override
    public List<CheckPointResultDto> transformCheckpoints(List<Location> locations, Route route) {
        if (CollectionUtils.isEmpty(route.getCheckPoints())) {
            return Collections.emptyList();
        }
        List<CheckPointResultDto> checkPoints = new ArrayList<>(route.getCheckPoints().size());
        for (CheckPoint checkPoint : route.getCheckPoints()) {
            CheckPointResultDto checkPointResultDto = operatorMapper.toCheckPointResultDto(checkPoint);
            ArrivalDepartureInfo arrivalDepartureInfo = OperatorLocationUtils.getArrivalAndDepartureTime(
                    checkPoint, locations);
            if (arrivalDepartureInfo != null) {
                checkPoints.add(checkPointResultDto);
                checkPointResultDto.setFactArrivalTime(arrivalDepartureInfo.getArrivalTime());
                checkPointResultDto.setFactDepartureTime(arrivalDepartureInfo.getDepartureTime());
                checkPointResultDto.setArrivalTime(checkPoint.getArrivalTime());
                checkPointResultDto.setDepartureTime(checkPoint.getDepartureTime());
            }
        }
        return checkPoints;
    }
}
