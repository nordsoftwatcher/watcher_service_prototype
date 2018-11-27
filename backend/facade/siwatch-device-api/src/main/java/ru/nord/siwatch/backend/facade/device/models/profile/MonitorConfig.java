package ru.nord.siwatch.backend.facade.device.models.profile;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.facade.device.models.monitoring.MonitorType;

import java.time.Duration;

@Getter @Setter
public class MonitorConfig
{
    private MonitorType type;
    private Duration pollInterval;
}
