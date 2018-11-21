package ru.nord.siwatch.backend.services.supervisor.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractEntityDto;

@Getter @Setter
public class SupervisorDto extends AbstractEntityDto {

    private String deviceId;

    private String name;

    private String middleName;

    private String lastName;
}
