package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.nord.siwatch.backend.facade.device.models.MonitorType;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter @Setter @ToString
public class MonitorRecordDto
{
    private MonitorType monitorType;
    private ZonedDateTime timestamp;
    private Object value;
}
