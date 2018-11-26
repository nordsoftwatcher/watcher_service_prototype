package ru.nord.siwatch.backend.facade.device.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter @Setter
public class MonitorConfig
{
    private MonitorType type;
    private Duration pollInterval;
}
