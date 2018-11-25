package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.nord.siwatch.backend.facade.device.models.EventPriority;
import ru.nord.siwatch.backend.facade.device.models.EventType;

import java.time.ZonedDateTime;

@Getter @Setter @ToString
public class EventRecordDto
{
    private EventType eventType;
    private EventPriority priority;
    private LocationDto location;
    private ZonedDateTime timestamp;
    private Object value;
}
