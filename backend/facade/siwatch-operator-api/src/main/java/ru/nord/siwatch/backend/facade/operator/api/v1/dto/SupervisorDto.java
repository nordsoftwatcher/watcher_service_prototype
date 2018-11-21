package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SupervisorDto extends AbstractDto {

    private String deviceId;

    private String name;

    private String middleName;

    private String lastName;
}
