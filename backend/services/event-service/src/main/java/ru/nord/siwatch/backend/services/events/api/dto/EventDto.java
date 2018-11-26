package ru.nord.siwatch.backend.services.events.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractEntityDto;

import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
public class EventDto extends AbstractEntityDto {

    private Long supervisorId;

    private LocalDateTime deviceTime;

    private LocalDateTime recordTime;

    private String eventType;

    private String eventValue;

    private Double latitude;

    private Double longitude;

}
