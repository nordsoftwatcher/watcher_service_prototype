package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.facade.device.models.MonitorConfig;
import ru.nord.siwatch.backend.facade.device.models.MonitorRecord;
import ru.nord.siwatch.backend.facade.device.models.MonitoringPolicy;
import ru.nord.siwatch.backend.facade.device.models.Profile;

import java.time.Duration;
import java.time.LocalDateTime;
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

    MonitorRecord getMonitorRecord(MonitorRecordDto dto);

    default LocalDateTime mapTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }
}
