package ru.nord.siwatch.backend.services.route.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter @Setter
public class CreateRoutePointInfoInput {

    private String name;

    @JsonFormat(pattern="HH:mm")
    private Date arrivalTime;

    @JsonFormat(pattern="HH:mm")
    private Date departureTime;

    private Integer planTime;

    private String address;

    private String description;

}
