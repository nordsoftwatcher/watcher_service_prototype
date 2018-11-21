package ru.nord.siwatch.backend.services.route.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import ru.nord.siwatch.backend.services.route.api.dto.RouteDto;
import ru.nord.siwatch.backend.services.route.api.model.CreateRouteInput;
import ru.nord.siwatch.backend.services.route.entities.Route;
import ru.nord.siwatch.backend.services.route.mapping.RouteMapper;
import ru.nord.siwatch.backend.services.route.repositories.RouteRepository;
import ru.nord.siwatch.backend.services.route.services.RouteService;

import javax.validation.Valid;

@Api(description = "Route API")
@RestController
@RequestMapping(value = ApiBase.PATH + RouteApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RouteApi extends ApiBase {

    public static final String PATH = "routes";

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RouteService routeService;

    @ApiOperation(value = "Создание маршрута")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public RouteDto createRoute(@Valid @RequestBody CreateRouteInput createRouteInput) {
        Route route = routeService.createRoute(createRouteInput);
        return routeMapper.toRouteDto(routeRepository.getRouteWithPoints(route.getId()));
    }

    @ApiOperation(value = "Получение информации о маршруте")
    @GetMapping( "/{routeId}")
    public RouteDto getRoute(@PathVariable Long routeId) {
        Route route = routeRepository.getRouteWithPoints(routeId);
        if (route != null) {
            RouteDto routeDto = routeMapper.toRouteDto(route);
            return routeDto;
        } else {
            return null;
        }
    }

}
