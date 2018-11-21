package ru.nord.siwatch.backend.services.route.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractEntityDto;

@Getter @Setter
public class RoutePointDto extends AbstractEntityDto {

    private Long order;

    private Double latitude;

    private Double longitude;

}
