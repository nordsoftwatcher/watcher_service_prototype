package ru.nord.siwatch.backend.facade.operator.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.facade.operator.services.DeviceLocationService;

import java.time.LocalDateTime;
import java.util.Map;

@Api(description = "OperatorApi")
@RestController
@RequestMapping(value = ApiBase.PATH + OperatorApi.PATH)
@Slf4j
public class OperatorApi extends ApiBase
{
    public static final String PATH = "operator";

    private final DeviceLocationService deviceLocationService;

    public OperatorApi(DeviceLocationService deviceLocationService)
    {

        this.deviceLocationService = deviceLocationService;
    }

    @ApiOperation(value = "Получение данных местоположения устройства")
    @GetMapping(value = "/device/{deviceId}/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getDeviceLocation(@PathVariable String deviceId, LocalDateTime since, LocalDateTime until)
    {
        return deviceLocationService.queryDeviceLocation(deviceId, since, until);
    }

    @ApiOperation(value = "Получение данных сердечного ритма с устройства")
    @GetMapping(value = "/device/{deviceId}/hr", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map getDeviceHeartRate(@PathVariable String deviceId, LocalDateTime since, LocalDateTime until)
    {
        return null;
    }
}
