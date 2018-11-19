package ru.nord.siwatch.backend.services.route.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter @Setter
public class CreateRouteInput {

    @Valid
    @NotEmpty(message = "Route has to contain at least one point")
    private List<CreateRoutePointInput> points;

}
