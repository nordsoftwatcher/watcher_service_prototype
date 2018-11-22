package ru.nord.siwatch.backend.facade.operator.mapping;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.CheckPointInfo;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.route.models.RouteInfo;
import ru.nord.siwatch.backend.connectors.route.models.RoutePointInfo;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.RouteDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.SupervisorDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.CreateCheckPointInput;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.CreateRouteInput;
import ru.nord.siwatch.backend.facade.operator.api.v1.model.CreateRoutePointInput;

import java.util.List;

@Mapper
public interface OperatorMapper {

    RouteInfo toRouteInfo(CreateRouteInput createRouteInput);

    RoutePointInfo toRoutePointInfo(CreateRoutePointInput createRoutePointInput);

    List<RoutePointInfo> toRoutePointInfoList(List<CreateRoutePointInput> createRoutePointInputs);

    CheckPointInfo toCheckPointInfo(CreateCheckPointInput createCheckPointInput);

    List<CheckPointInfo> toCheckPointInfoList(List<CreateCheckPointInput> createCheckPointInputs);

    RouteDto toRouteDto(Route route);

    SupervisorDto toSupervisorDto(Supervisor supervisor);

    LocationDto toLocationDto(Location location);

    List<LocationDto> toLocationDtoList(List<Location> locations);

}
