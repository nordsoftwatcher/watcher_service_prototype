package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter @ToString(callSuper = true)
public class SyncPacketDto extends MessagePacketDto
{
    private String deviceId;
    private List<MonitorRecordDto> monitors;
}
