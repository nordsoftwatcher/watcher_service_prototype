package ru.nord.siwatch.backend.facade.operator.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.RouteDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.CreateRouteInput;
import ru.nord.siwatch.backend.facade.operator.mapping.OperatorMapper;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;
import ru.nord.siwatch.backend.facade.operator.services.SupervisorService;
import javax.validation.Valid;

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

    @ApiOperation(value = "Создание маршрута")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Route createRoute(@Valid @RequestBody CreateRouteInput createRouteInput) {
        Supervisor supervisor = supervisorService.getSupervisorById(createRouteInput.getSupervisorId());
        if (supervisor == null) {
            throw new RuntimeException("Supervisor with id " + createRouteInput.getSupervisorId() + " doesn't exist");
        }
        return routeService.createRoute(createRouteInput);
    }

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
