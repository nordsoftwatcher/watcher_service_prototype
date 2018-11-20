package ru.nord.siwatch.backend.services.common.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public abstract class AbstractRecordSearchDto
{
    @ApiModelProperty(notes = "ID устройства", example = "TODO", position = 0)
    @NotBlank
    private String deviceId;

    @ApiModelProperty(notes = "Время начала", example = "TODO", position = 10)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromTime;

    @ApiModelProperty(notes = "Время окончания", example = "TODO", position = 20)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toTime;
}
