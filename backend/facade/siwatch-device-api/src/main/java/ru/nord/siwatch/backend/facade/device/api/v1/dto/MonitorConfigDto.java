package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.facade.device.models.monitoring.MonitorType;

@Getter @Setter
public class MonitorConfigDto
{
    private MonitorType type;
    private long pollInterval;
}
