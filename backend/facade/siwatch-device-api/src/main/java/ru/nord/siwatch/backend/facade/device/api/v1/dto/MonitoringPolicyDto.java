package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Getter @Setter
public class MonitoringPolicyDto
{
    private int minBatteryLevel;
    private List<MonitorConfigDto> monitors;
    private long syncInterval;
    private int packetSize;
}
