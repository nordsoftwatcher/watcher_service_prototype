package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.nord.siwatch.backend.facade.device.models.events.EventPriority;
import ru.nord.siwatch.backend.facade.device.models.events.DeviceEventType;

import java.time.ZonedDateTime;

@Getter @Setter @ToString
public class EventRecordDto
{
    private DeviceEventType eventType;
    private EventPriority priority;
    private LocationDto location;
    private ZonedDateTime timestamp;
    private Object value;
}
