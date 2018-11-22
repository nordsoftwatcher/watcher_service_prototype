package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.facade.device.models.profile.*;

import java.time.Duration;

@Mapper
public interface ProfileDtoMapper
{
    default long getDurationInSeconds(Duration duration)
    {
        return duration.toMillis() / 1000;
    }

    MonitorConfigDto getMonitorConfigDto(MonitorConfig config);
    MonitoringPolicyDto getMonitoringPolicyDto(MonitoringPolicy policy);
    ProfileDto getProfileDto(Profile profile);
}
