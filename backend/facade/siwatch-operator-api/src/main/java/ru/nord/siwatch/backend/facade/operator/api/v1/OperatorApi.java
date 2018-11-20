package ru.nord.siwatch.backend.facade.operator.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.facade.operator.services.DeviceLocationService;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;

import java.time.LocalDateTime;
import java.util.Map;

@Api(description = "OperatorApi")
@RestController
@RequestMapping(value = ApiBase.PATH + OperatorApi.PATH)
@Slf4j
public class OperatorApi extends ApiBase {

    public static final String PATH = "operator";

    @Autowired
    private DeviceLocationService deviceLocationService;

    @Autowired
    private RouteService routeService;

    @ApiOperation(value = "Получение данных местоположения устройства")
    @GetMapping(value = "/device/{deviceId}/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getDeviceLocation(@PathVariable String deviceId, LocalDateTime since, LocalDateTime until) {
        return deviceLocationService.queryDeviceLocation(deviceId, since, until);
    }

    @ApiOperation(value = "Получение данных сердечного ритма с устройства")
    @GetMapping(value = "/device/{deviceId}/hr", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map getDeviceHeartRate(@PathVariable String deviceId, LocalDateTime since, LocalDateTime until) {
        return null;
    }

    @ApiOperation(value = "Получение маршрута по идентификатору")
    @GetMapping(value = "/route/{routeId}")
    public Route getRoute(@PathVariable Long routeId) {
        return routeService.getRouteById(routeId);
    }

}
