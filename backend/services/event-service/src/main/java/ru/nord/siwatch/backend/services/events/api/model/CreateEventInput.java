package ru.nord.siwatch.backend.services.events.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class CreateEventInput {

    @NotNull(message = "Supervisor id can't be null")
    private Long supervisorId;

    @NotBlank(message = "Event's name can't be blank")
    private String name;

}
