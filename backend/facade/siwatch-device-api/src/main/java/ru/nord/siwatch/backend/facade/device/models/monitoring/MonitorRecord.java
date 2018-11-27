package ru.nord.siwatch.backend.facade.device.models.monitoring;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class MonitorRecord
{
    private MonitorType monitorType;
    private LocalDateTime timestamp;
    private Object value;
}
