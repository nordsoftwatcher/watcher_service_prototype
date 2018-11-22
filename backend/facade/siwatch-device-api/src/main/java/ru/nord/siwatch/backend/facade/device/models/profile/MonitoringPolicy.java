package ru.nord.siwatch.backend.facade.device.models.profile;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Getter @Setter
public class MonitoringPolicy
{
    private int minBatterLevel;
    private List<MonitorConfig> monitors;
    private Duration flushInterval;
    private int packetSize;
}
