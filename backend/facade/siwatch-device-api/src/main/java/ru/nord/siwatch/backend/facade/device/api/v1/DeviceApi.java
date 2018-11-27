package ru.nord.siwatch.backend.facade.device.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.DtoMapper;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.MessagePacketDto;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.ProfileDto;
import ru.nord.siwatch.backend.facade.device.api.v1.dto.SyncPacketDto;
import ru.nord.siwatch.backend.facade.device.models.MessagePacket;
import ru.nord.siwatch.backend.facade.device.models.SyncPacket;
import ru.nord.siwatch.backend.facade.device.services.DeviceProfileService;
import ru.nord.siwatch.backend.facade.device.services.DeviceSyncService;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api(description = "DeviceApi")
@RestController
@RequestMapping(value = ApiBase.PATH + DeviceApi.PATH)
@Slf4j
public class DeviceApi extends ApiBase
{
    static final String PATH = "device";

    private final DeviceProfileService profileService;
    private final DeviceSyncService syncService;
    private final DtoMapper mapper;

    public DeviceApi(
        DeviceProfileService profileService,
        DeviceSyncService syncService,
        DtoMapper mapper)
    {
        this.profileService = profileService;
        this.syncService = syncService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Получение профиля устройства")
    @GetMapping(value = "profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDto getProfile(@NotBlank String deviceId)
    {
        log.info("Profile request by device '"+ Objects.toString(deviceId)+"'");
        return mapper.getProfileDto(profileService.getDeviceProfile(deviceId));
    }

    @ApiOperation(value = "Синхронизация с устройством")
    @PutMapping(value = "sync", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MessagePacketDto sync(@RequestBody SyncPacketDto[] input)
    {
        log.info("Sync receive: "+ Objects.toString(input));

        final MessagePacket pack = new MessagePacket();
        pack.setEvents(new ArrayList<>());
        for (SyncPacketDto syncPacket : input) {
            final SyncPacket packet = mapper.getSyncPacket(syncPacket);
            final MessagePacket message = syncService.sync(packet);
            if(message != null && message.getEvents() != null) {
                pack.getEvents().addAll(message.getEvents());
            }
        }
        pack.setTimestamp(LocalDateTime.now());

        final MessagePacketDto output = mapper.getMessagePacketDto(pack);
        log.info("Sync send: "+ Objects.toString(output));

        return output;
    }
}
