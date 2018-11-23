package ru.nord.siwatch.backend.services.route.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractEntityDto;
import ru.nord.siwatch.backend.services.route.enums.RouteStatus;
import java.util.List;

@Getter @Setter
public class RouteDto extends AbstractEntityDto {

    private String name;

    private RouteStatus status;

    private Long supervisorId;

    private List<RoutePointDto> routePoints;

    private List<CheckPointDto> checkPoints;

}
