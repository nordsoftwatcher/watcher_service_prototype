package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.facade.device.models.Location;
import ru.nord.siwatch.backend.facade.device.models.MessagePacket;
import ru.nord.siwatch.backend.facade.device.models.SyncPacket;
import ru.nord.siwatch.backend.facade.device.models.events.EventRecord;
import ru.nord.siwatch.backend.facade.device.models.profile.MonitorConfig;
import ru.nord.siwatch.backend.facade.device.models.monitoring.MonitorRecord;
import ru.nord.siwatch.backend.facade.device.models.profile.MonitoringPolicy;
import ru.nord.siwatch.backend.facade.device.models.profile.Profile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper
public interface DtoMapper
{
    default long getDurationInSeconds(Duration duration)
    {
        return duration.toMillis() / 1000;
    }

    MonitorConfigDto getMonitorConfigDto(MonitorConfig config);
    MonitoringPolicyDto getMonitoringPolicyDto(MonitoringPolicy policy);
    ProfileDto getProfileDto(Profile profile);

    Location getLocation(LocationDto dto);

    EventRecord getEventRecord(EventRecordDto dto);

    MonitorRecord getMonitorRecord(MonitorRecordDto dto);

    MessagePacketDto getMessagePacketDto(MessagePacket packet);
    SyncPacket getSyncPacket(SyncPacketDto dto);

    default LocalDateTime mapZonedTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }
    default ZonedDateTime mapLocalTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault());
    }
}
