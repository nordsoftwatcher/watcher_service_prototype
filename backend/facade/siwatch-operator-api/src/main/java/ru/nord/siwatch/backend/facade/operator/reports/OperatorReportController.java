package ru.nord.siwatch.backend.facade.operator.reports;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.CheckPoint;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.facade.operator.api.v1.ApiBase;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.CheckPointResultDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationDto;
import ru.nord.siwatch.backend.facade.operator.mapping.OperatorMapper;
import ru.nord.siwatch.backend.facade.operator.reports.dto.CheckPointPassedDto;
import ru.nord.siwatch.backend.facade.operator.reports.dto.RouteDeviationDto;
import ru.nord.siwatch.backend.facade.operator.services.DeviceLocationService;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;
import ru.nord.siwatch.backend.facade.operator.services.SupervisorService;
import ru.nord.siwatch.backend.facade.operator.utils.OperatorLocationUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/reports")
public class OperatorReportController extends ApiBase {

    private static final Double DEVIATION_THRESHOLD = 0.00025;

    @Autowired
    private RouteService routeService;

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private DeviceLocationService deviceLocationService;

    @Autowired
    private OperatorMapper operatorMapper;

    @GetMapping("/route_report/{routeId}")
    public String routeReport(@PathVariable Long routeId,  Model model) {
        Route route = routeService.getRouteById(routeId);
        if (route == null) {
            throw new RuntimeException("No route with ID " + routeId);
        }
        if (route.getStartTime() == null || route.getEndTime() == null) {
            throw new RuntimeException("Route doesn't have start or end time");
        }
        Supervisor supervisor = supervisorService.getSupervisorById(route.getSupervisorId());
        if (supervisor == null) {
            throw new RuntimeException("No supervisor with ID " + route.getSupervisorId());
        }
        /** Process data */
        List<Location> locations = deviceLocationService.queryDeviceLocation(supervisor.getDeviceId(), null, null);
        locations.sort(Comparator.comparing(Location::getDeviceTime));
        /** Transform locations and checkpoints */
        List<LocationDto> transformedLocations = routeService.transformLocations(locations, route);
        List<CheckPointResultDto> checkPointsResults = routeService.transformCheckpoints(locations, route);
        List<CheckPointPassedDto> checkPointPassedInfos = getCheckPointPassedInformation(checkPointsResults, route.getCheckPoints());
        /** Set attributes */
        model.addAttribute("routeName", route.getName());
        model.addAttribute("supervisorName", supervisor.getName() + " " + supervisor.getMiddleName() + " " + supervisor.getLastName());
        model.addAttribute("routeStartTime", route.getStartTime() != null ? Date.from(route.getStartTime().toInstant(ZoneOffset.UTC)) : null);
        model.addAttribute("routeEndTime", route.getEndTime() != null ? Date.from(route.getEndTime().toInstant(ZoneOffset.UTC)) : null);
        model.addAttribute("routeDeviations", getRouteDeviations(transformedLocations));
        model.addAttribute("checkPointPassedInfos", checkPointPassedInfos);
        model.addAttribute("checkPointPassedCount", checkPointPassedInfos.stream().filter(checkPointPassed -> checkPointPassed.getPassed()).count());
        model.addAttribute("checkPointNotPassedCount", checkPointPassedInfos.stream().filter(checkPointPassed -> !checkPointPassed.getPassed()).count());
        return "route_report";
    }

    private List<RouteDeviationDto> getRouteDeviations(List<LocationDto> locations) {
        List<RouteDeviationDto> deviations = new ArrayList<>();
        LocalDateTime currentStartTime = null;

        for (LocationDto location : locations) {
            if (DEVIATION_THRESHOLD < location.getRouteDistance()) {
                if (currentStartTime == null) {
                    currentStartTime = location.getDeviceTime();
                }
            } else {
                if (currentStartTime != null) {
                    deviations.add(new RouteDeviationDto(
                            Date.from(currentStartTime.toInstant(ZoneOffset.UTC)),
                            Date.from(location.getDeviceTime().toInstant(ZoneOffset.UTC)),
                            OperatorLocationUtils.calcTimeInMinutes(currentStartTime, location.getDeviceTime())
                    ));
                    currentStartTime = null;
                }
            }
        }

        if (currentStartTime != null) {
            deviations.add(new RouteDeviationDto(
                    Date.from(currentStartTime.toInstant(ZoneOffset.UTC)), null, null
            ));
        }
        return deviations;
    }

    private List<CheckPointPassedDto> getCheckPointPassedInformation(List<CheckPointResultDto> checkPointResults, List<CheckPoint> checkPoints) {
        List<CheckPointPassedDto> result = new ArrayList<>(checkPoints.size());
        for (CheckPoint checkPoint : checkPoints) {
            CheckPointResultDto checkPointResult = null;
            /** Search checkpoint result */
            for (CheckPointResultDto checkPointResultDto : checkPointResults) {
                if (checkPoint.getId().equals(checkPointResultDto.getId())) {
                    checkPointResult = checkPointResultDto;
                    break;
                }
            }
            if (checkPointResult != null) {
                result.add(new CheckPointPassedDto(
                        checkPointResult.getName(), checkPointResult.getAddress(), checkPoint.getDescription(),
                        checkPointResult.getArrivalTime() != null ?  Date.from(checkPointResult.getArrivalTime().toInstant(ZoneOffset.UTC)) : null,
                        checkPointResult.getDepartureTime() != null ?  Date.from(checkPointResult.getDepartureTime().toInstant(ZoneOffset.UTC)) : null,
                        checkPointResult.getFactArrivalTime() != null ?  Date.from(checkPointResult.getFactArrivalTime().toInstant(ZoneOffset.UTC)) : null,
                        checkPointResult.getFactDepartureTime() != null ?  Date.from(checkPointResult.getFactDepartureTime().toInstant(ZoneOffset.UTC)) : null,
                        (checkPointResult.getFactArrivalTime() != null && checkPointResult.getFactDepartureTime() != null)
                ));
            } else {
                result.add(new CheckPointPassedDto(
                        checkPoint.getName(), checkPoint.getAddress(), checkPoint.getDescription(),
                        checkPoint.getArrivalTime() != null ?  Date.from(checkPoint.getArrivalTime().toInstant(ZoneOffset.UTC)) : null,
                        checkPoint.getDepartureTime() != null ?  Date.from(checkPoint.getDepartureTime().toInstant(ZoneOffset.UTC)) : null,
                        null, null, false
                ));
            }
        }
        return result;
    }

}
