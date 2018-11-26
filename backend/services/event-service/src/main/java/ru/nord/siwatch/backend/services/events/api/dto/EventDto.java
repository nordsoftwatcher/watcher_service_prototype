package ru.nord.siwatch.backend.services.events.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractEntityDto;

import java.util.Date;

@Getter @Setter
public class EventDto extends AbstractEntityDto {

    private Long supervisorId;

    private Date recordTime;

    private String name;

}
