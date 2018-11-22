package ru.nord.siwatch.backend.facade.operator.mapping;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.RouteDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.SupervisorDto;
import java.util.List;

@Mapper
public interface OperatorMapper {

    RouteDto toRouteDto(Route route);

    SupervisorDto toSupervisorDto(Supervisor supervisor);

    LocationDto toLocationDto(Location location);

    List<LocationDto> toLocationDtoList(List<Location> locations);

}
