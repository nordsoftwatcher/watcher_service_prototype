package ru.nord.siwatch.backend.services.route.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractEntityDto;
import java.util.Date;

@Getter
@Setter
public class CheckPointDto extends AbstractEntityDto {

    private Double radius;

    private Long order;

    private Double latitude;

    private Double longitude;

    private String name;

    private Date arrivalTime;

    private Date departureTime;

    private Integer planTime;

    private Integer factTime;

    private String address;

    private String description;

}
