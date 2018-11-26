package ru.nord.siwatch.backend.facade.device.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class MonitorRecord
{
    private MonitorType monitorType;
    private LocalDateTime timestamp;
    private Object value;
}
