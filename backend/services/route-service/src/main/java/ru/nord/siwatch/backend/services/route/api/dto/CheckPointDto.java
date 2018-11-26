package ru.nord.siwatch.backend.services.route.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractEntityDto;
import java.time.LocalDateTime;

@Getter
@Setter
public class CheckPointDto extends AbstractEntityDto {

    private Double radius;

    private Long order;

    private Double latitude;

    private Double longitude;

    private String name;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private String address;

    private String description;

}
