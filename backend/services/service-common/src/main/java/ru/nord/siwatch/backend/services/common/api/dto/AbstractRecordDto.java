package ru.nord.siwatch.backend.services.common.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public abstract class AbstractRecordDto
{
    @ApiModelProperty(notes = "ID устройства", example = "TODO", position = 0)
    private String deviceId;
    @ApiModelProperty(notes = "Время фиксации показаний на устройстве", example = "TODO", position = 1)
    private LocalDateTime deviceTime;
    @ApiModelProperty(notes = "Время сохранения показаний", example = "TODO", position = 2)
    private LocalDateTime recordTime;
}
