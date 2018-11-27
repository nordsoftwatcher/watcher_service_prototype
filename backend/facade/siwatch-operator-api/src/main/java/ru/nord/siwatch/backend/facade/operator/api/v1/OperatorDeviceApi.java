package ru.nord.siwatch.backend.facade.operator.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.CheckPoint;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.ArrivalDepartureInfo;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.CheckPointResultDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationRecordSearchDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.DeviceLocationOutput;
import ru.nord.siwatch.backend.facade.operator.mapping.OperatorMapper;
import ru.nord.siwatch.backend.facade.operator.services.DeviceLocationService;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;
import ru.nord.siwatch.backend.facade.operator.services.SupervisorService;
import ru.nord.siwatch.backend.facade.operator.utils.OperatorLocationUtils;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
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
    public DeviceLocationOutput getDeviceLocation(@Valid LocationRecordSearchDto recordSearchDto) {
        List<Location> locations = deviceLocationService.queryDeviceLocation(recordSearchDto.getDeviceId(), recordSearchDto.getFromTime(), recordSearchDto.getToTime());
        locations.sort(Comparator.comparing(Location::getDeviceTime));
        /** Transform data */
        if (recordSearchDto.getRouteId() != null) {
            Route route = routeService.getRouteById(recordSearchDto.getRouteId());
            if (route != null) {
                /** Transform locations */
                List<LocationDto> result = routeService.transformLocations(locations, route);
                /** Transform checkpoints */
                List<CheckPointResultDto> checkPoints = routeService.transformCheckpoints(locations, route);
                return new DeviceLocationOutput(result, checkPoints);
            } else {
                throw new RuntimeException("Route with id " + recordSearchDto.getRouteId() + " hasn't been found");
            }
        } else {
            return new DeviceLocationOutput(operatorMapper.toLocationDtoList(locations), null);
        }
    }

    @ApiOperation(value = "Получение данных сердечного ритма с устройства")
    @GetMapping(value = "/{deviceId}/hr", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map getDeviceHeartRate(@PathVariable String deviceId, LocalDateTime since, LocalDateTime until) {
        return null;
    }

}
