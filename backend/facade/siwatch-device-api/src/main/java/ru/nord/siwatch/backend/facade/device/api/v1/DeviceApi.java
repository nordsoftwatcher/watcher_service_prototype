package ru.nord.siwatch.backend.facade.device.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "DeviceApi")
@RestController
@RequestMapping(value = ApiBase.PATH + DeviceApi.PATH)
@Slf4j
public class DeviceApi extends ApiBase
{
    public static final String PATH = "device";

    public DeviceApi()
    {

    }

    @ApiOperation(value = "Получение профиля устройства")
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map getProfile()
    {
        return null;
    }

    @ApiOperation(value = "Обработка данных с устройства")
    @PutMapping(value = "/data", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processData(@RequestBody Map<String, Object> data)
    {
        return ResponseEntity.ok().build();
    }
}
