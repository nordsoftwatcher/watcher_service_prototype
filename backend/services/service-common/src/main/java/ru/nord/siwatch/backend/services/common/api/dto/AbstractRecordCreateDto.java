package ru.nord.siwatch.backend.services.common.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter @Setter
public abstract class AbstractRecordCreateDto
{
    @ApiModelProperty(notes = "ID устройства", example = "TEST_DEVICE")
    @NotBlank
    @Size(max = 32)
    private String deviceId;

    @ApiModelProperty(notes = "Время фиксации показания на устройстве", example = "2018-11-20T15:00:00")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deviceTime;
}
