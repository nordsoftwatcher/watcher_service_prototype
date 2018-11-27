package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.nord.siwatch.backend.facade.device.models.events.EventRecord;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter @ToString
public class MessagePacketDto
{
    private ZonedDateTime timestamp;
    private List<EventRecordDto> events;
}
