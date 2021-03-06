package ru.nord.siwatch.backend.services.route.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class CreateCheckPointInput {

    @NotNull(message = "Radius can't be null")
    private Double radius;

    @NotNull(message = "Order can't be null")
    private Long order;

    @NotNull(message = "Latitude can't be null")
    private Double latitude;

    @NotNull(message = "Longitude can't be null")
    private Double longitude;

    private String name;

    @ApiModelProperty(example = "2018-11-20T15:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime arrivalTime;

    @ApiModelProperty(example = "2018-11-20T15:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departureTime;

    private String address;

    private String description;

}
