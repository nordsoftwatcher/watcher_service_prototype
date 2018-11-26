package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter @Setter
public class SearchEventsDto {

    private Long supervisorId;

    @ApiModelProperty(notes = "Время начала", example = "2018-11-20T15:00:00", position = 10)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromTime;

    @ApiModelProperty(notes = "Время окончания", example = "2018-11-20T16:00:00", position = 20)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toTime;

}
