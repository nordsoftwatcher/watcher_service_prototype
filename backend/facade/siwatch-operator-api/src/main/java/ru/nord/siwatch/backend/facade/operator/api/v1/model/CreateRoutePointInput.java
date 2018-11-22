package ru.nord.siwatch.backend.facade.operator.api.v1.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class CreateRoutePointInput {

    @NotNull(message = "Order can't be null")
    private Long order;

    @NotNull(message = "Latitude can't be null")
    private Double latitude;

    @NotNull(message = "Longitude can't be null")
    private Double longitude;

}
