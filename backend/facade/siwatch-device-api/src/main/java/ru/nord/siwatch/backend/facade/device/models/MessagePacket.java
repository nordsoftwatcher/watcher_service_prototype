package ru.nord.siwatch.backend.facade.device.models;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.facade.device.models.events.EventRecord;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MessagePacket
{
    private LocalDateTime timestamp;

    private List<EventRecord> events;
}
