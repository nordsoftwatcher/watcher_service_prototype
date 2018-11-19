package ru.nord.siwatch.backend.services.route.api.model;

import lombok.Getter;
import lombok.Setter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class CreateRoutePointInput {

    @NotNull(message = "Order can't be null")
    private Long order;

    @NotNull(message = "Latitude can't be null")
    private Double latitude;

    @NotNull(message = "Longitude can't be null")
    private Double longitude;

    @NotNull(message = "Altitude can't be null")
    private Double altitude;

    @Valid
    private CreateRoutePointInfoInput pointInfo;
}
