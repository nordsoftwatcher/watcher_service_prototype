package ru.nord.siwatch.backend.services.common.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AbstractRecordSearchDto {

    @ApiModelProperty(notes = "ID устройства", example = "TEST_DEVICE", position = 0)
    @NotBlank
    private String deviceId;

    @ApiModelProperty(notes = "Время начала", example = "2018-11-20T15:00:00", position = 10)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromTime;

    @ApiModelProperty(notes = "Время окончания", example = "2018-11-20T16:00:00", position = 20)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toTime;
}
