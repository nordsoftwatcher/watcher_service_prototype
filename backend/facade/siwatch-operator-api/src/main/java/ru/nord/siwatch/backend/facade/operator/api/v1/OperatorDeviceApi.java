package ru.nord.siwatch.backend.facade.operator.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationRecordSearchDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.RouteDto;
import ru.nord.siwatch.backend.facade.operator.mapping.OperatorMapper;
import ru.nord.siwatch.backend.facade.operator.services.DeviceLocationService;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;
import ru.nord.siwatch.backend.facade.operator.services.SupervisorService;
import ru.nord.siwatch.backend.facade.operator.utils.LocationUtils;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(description = "OperatorDeviceApi")
@RestController
@RequestMapping(value = ApiBase.PATH + OperatorDeviceApi.PATH)
@Slf4j
public class OperatorDeviceApi extends ApiBase {

    public static final String PATH = "operator/device";

    @Autowired
    private DeviceLocationService deviceLocationService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private OperatorMapper operatorMapper;

    @ApiOperation(value = "Получение данных местоположения устройства")
    @GetMapping(value = "/location")
    public List<LocationDto> getDeviceLocation(@Valid LocationRecordSearchDto recordSearchDto) {
        List<Location> locations = deviceLocationService.queryDeviceLocation(recordSearchDto.getDeviceId(), recordSearchDto.getFromTime(), recordSearchDto.getToTime());
        if (recordSearchDto.getRouteId() != null) {
            Route route = routeService.getRouteById(recordSearchDto.getRouteId());
            if (route != null) {
                List<LocationDto> result = new ArrayList<>(locations.size());
                for (Location location : locations) {
                    LocationDto tempLocation = operatorMapper.toLocationDto(location);
                    tempLocation.setRouteDistance(LocationUtils.distanceFromRoute(route, location));
                    result.add(tempLocation);
                }
                return result;
            } else {
                throw new RuntimeException("Route with id " + recordSearchDto.getRouteId() + " hasn't been found");
            }
        } else {
            return operatorMapper.toLocationDtoList(locations);
        }
    }

    @ApiOperation(value = "Получение данных сердечного ритма с устройства")
    @GetMapping(value = "/{deviceId}/hr", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map getDeviceHeartRate(@PathVariable String deviceId, LocalDateTime since, LocalDateTime until) {
        return null;
    }

}
