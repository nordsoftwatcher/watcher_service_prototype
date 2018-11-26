package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter @ToString
public class SyncPacketDto
{
    @NotBlank
    private String deviceId;

    private ZonedDateTime timestamp;

    private List<MonitorRecordDto> monitors;

    private List<EventRecordDto> events;
}
