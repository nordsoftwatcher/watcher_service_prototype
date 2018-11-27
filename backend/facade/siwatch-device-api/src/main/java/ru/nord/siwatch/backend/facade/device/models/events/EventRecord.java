package ru.nord.siwatch.backend.facade.device.models.events;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.facade.device.models.Location;

import java.time.LocalDateTime;

@Getter @Setter
public class EventRecord
{
    private DeviceEventType eventType;
    private EventPriority priority;
    private Location location;
    private LocalDateTime timestamp;
    private Object value;
}
