package ru.nord.siwatch.backend.facade.device.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.EventRecordDto;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.ProfileDto;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.DtoMapper;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.SyncPacketDto;
import ru.nord.siwatch.backend.facade.device.models.MonitorRecord;
import ru.nord.siwatch.backend.facade.device.services.DeviceProfileService;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Api(description = "DeviceApi")
@RestController
@RequestMapping(value = ApiBase.PATH + DeviceApi.PATH)
@Slf4j
public class DeviceApi extends ApiBase
{
    static final String PATH = "device";

    private final DeviceProfileService deviceProfileService;
    private final DtoMapper mapper;

    public DeviceApi(DeviceProfileService deviceProfileService, DtoMapper mapper)
    {
        this.deviceProfileService = deviceProfileService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Получение профиля устройства")
    @GetMapping(value = "profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDto getProfile(@NotBlank String deviceId)
    {
        log.info("Profile sent to "+ deviceId);
        return mapper.getProfileDto(deviceProfileService.getDeviceProfile(deviceId));
    }

    @ApiOperation(value = "Синхронизация с устройством")
    @PutMapping(value = "sync", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SyncPacketDto sync(@RequestBody SyncPacketDto packet)
    {
        log.info("Sync: "+ Objects.toString(packet));

        if(packet.getMonitors() != null) {
            List<MonitorRecord> records = packet.getMonitors().stream().map(mapper::getMonitorRecord).collect(Collectors.toList());
            deviceProfileService.saveMonitors(packet.getDeviceId(), records);
        }

        SyncPacketDto response = new SyncPacketDto();
        response.setTimestamp(ZonedDateTime.now());
        //response.setEvents(new ArrayList<EventRecordDto>());
        //EventRecordDto eventRecordDto = new EventRecordDto();
        //eventRecordDto.setEventType(EventType.Message);
        //eventRecordDto.setValue("It is "+new Date() + " now!");
        //response.getEvents().add(eventRecordDto);
        return response;
    }
}
