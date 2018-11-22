package ru.nord.siwatch.backend.facade.operator.api.v1.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class CreateRouteInput {

    @NotNull(message = "Supervisor id can't be empty")
    private Long supervisorId;

    @Valid
    @NotEmpty(message = "Route has to contain at least one point")
    private List<CreateRoutePointInput> routePoints;

    @Valid
    @NotEmpty(message = "Route has to contain at least one check point")
    private List<CreateCheckPointInput> checkPoints;

}
