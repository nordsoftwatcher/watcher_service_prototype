package ru.nord.siwatch.backend.connectors.supervisor.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Supervisor {

    private Long id;

    private String deviceId;

    private String name;

    private String middleName;

    private String lastName;

}
