package ru.nord.siwatch.backend.facade.operator.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.RouteDto;
import ru.nord.siwatch.backend.facade.operator.mapping.OperatorMapper;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;
import ru.nord.siwatch.backend.facade.operator.services.SupervisorService;

@Api(description = "OperatorRouteApi")
@RestController
@RequestMapping(value = ApiBase.PATH + OperatorRouteApi.PATH)
@Slf4j
public class OperatorRouteApi extends ApiBase {

    public static final String PATH = "operator/route";

    @Autowired
    private RouteService routeService;

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private OperatorMapper operatorMapper;

    @ApiOperation(value = "Получение маршрута по идентификатору")
    @GetMapping(value = "/{routeId}")
    public RouteDto getRoute(@PathVariable Long routeId) {
        Route route = routeService.getRouteById(routeId);
        if (route != null) {
            Supervisor supervisor = supervisorService.getSupervisorById(route.getSupervisorId());
            if (supervisor != null) {
                RouteDto routeDto = operatorMapper.toRouteDto(route);
                routeDto.setSupervisor(operatorMapper.toSupervisorDto(supervisor));
                return routeDto;
            } else {
                throw new RuntimeException("Supervisor with id " + route.getSupervisorId() + " doesn't exist");
            }
        } else {
            throw new RuntimeException("Route with id " + routeId + " hasn't been found");
        }
    }
}
