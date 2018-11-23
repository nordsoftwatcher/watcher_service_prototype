package ru.nord.siwatch.backend.facade.device.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.ProfileDto;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.ProfileDtoMapper;
import ru.nord.siwatch.backend.facade.device.services.DeviceProfileService;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Objects;

@Api(description = "DeviceApi")
@RestController
@RequestMapping(value = ApiBase.PATH + DeviceApi.PATH)
@Slf4j
public class DeviceApi extends ApiBase
{
    static final String PATH = "device";

    private final DeviceProfileService deviceProfileService;
    private final ProfileDtoMapper profileMapper;

    public DeviceApi(DeviceProfileService deviceProfileService, ProfileDtoMapper profileMapper)
    {
        this.deviceProfileService = deviceProfileService;
        this.profileMapper = profileMapper;
    }

    @ApiOperation(value = "Получение профиля устройства")
    @GetMapping(value = "profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDto getProfile(@NotBlank String deviceId)
    {
        return profileMapper.getProfileDto(deviceProfileService.getDeviceProfile(deviceId));
    }

    @ApiOperation(value = "Обработка данных с устройства")
    @PutMapping(value = "sync", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processData(@RequestBody Map<String, Object> data)
    {
        log.info("Got "+ Objects.toString(data));
        return ResponseEntity.ok().build();
    }
}
