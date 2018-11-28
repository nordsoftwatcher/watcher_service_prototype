package ru.nord.siwatch.backend.facade.operator.reports;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
import ru.nord.siwatch.backend.facade.operator.reports.dto.RouteIntervalDto;
import ru.nord.siwatch.backend.facade.operator.services.DeviceLocationService;
import ru.nord.siwatch.backend.facade.operator.services.RouteService;
import ru.nord.siwatch.backend.facade.operator.services.SupervisorService;
import ru.nord.siwatch.backend.facade.operator.utils.OperatorLocationUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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
        List<RouteIntervalDto> routeIntervals = getRouteIntervals(route, locations, checkPointsResults);
        /** Set attributes */
        model.addAttribute("routeName", route.getName());
        model.addAttribute("supervisorName", supervisor.getName() + " " + supervisor.getMiddleName() + " " + supervisor.getLastName());
        model.addAttribute("routeStartTime", getRouteStartTime(routeIntervals));
        model.addAttribute("routeEndTime", getRouteEndTime(routeIntervals));
        model.addAttribute("routeDeviations", getRouteDeviations(transformedLocations));
        model.addAttribute("checkPointPassedInfos", checkPointPassedInfos);
        model.addAttribute("checkPointPassedCount", checkPointPassedInfos.stream().filter(checkPointPassed -> checkPointPassed.getPassed()).count());
        model.addAttribute("checkPointNotPassedCount", checkPointPassedInfos.stream().filter(checkPointPassed -> !checkPointPassed.getPassed()).count());
        model.addAttribute("routeIntervals", routeIntervals);
        model.addAttribute("totalIntervalTime", getTotalIntervalTime(routeIntervals));
        model.addAttribute("factTotalIntervalTime", getFactTotalIntervalTime(routeIntervals));
        return "route_report";
    }

    private Integer getTotalIntervalTime(List<RouteIntervalDto> routeIntervals) {
        Integer result = 0;
        for (RouteIntervalDto routeInterval : routeIntervals) {
            result += (routeInterval.getPlanTimeMinutes());
        }
        return result;
    }

    private Integer getFactTotalIntervalTime(List<RouteIntervalDto> routeIntervals) {
        Integer result = 0;
        for (RouteIntervalDto routeInterval : routeIntervals) {
            result += (routeInterval.getFactTimeMinutes());
        }
        return result;
    }

    private Date getRouteStartTime(List<RouteIntervalDto> routeIntervals) {
        if (CollectionUtils.isEmpty(routeIntervals)) {
            return null;
        }
        RouteIntervalDto routeInterval = routeIntervals.get(0);
        return routeInterval.getStartTime();
    }

    private Date getRouteEndTime(List<RouteIntervalDto> routeIntervals) {
        if (CollectionUtils.isEmpty(routeIntervals)) {
            return null;
        }
        RouteIntervalDto routeInterval = routeIntervals.get(routeIntervals.size() - 1);
        return routeInterval.getEndTime();
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
                            OperatorLocationUtils.getDateFromLocalDateTime(currentStartTime),
                            OperatorLocationUtils.getDateFromLocalDateTime(location.getDeviceTime()),
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
            CheckPointResultDto checkPointResult = getCheckpointResult(checkPoint, checkPointResults);
            if (checkPointResult != null) {
                CheckPointPassedDto passedDto = new CheckPointPassedDto();
                passedDto.setName(checkPointResult.getName());
                passedDto.setAddress(checkPointResult.getAddress());
                passedDto.setDescription(checkPoint.getDescription());
                passedDto.setArrivalTime(OperatorLocationUtils.getDateFromLocalDateTime(checkPointResult.getArrivalTime()));
                passedDto.setDepartureTime(OperatorLocationUtils.getDateFromLocalDateTime(checkPointResult.getDepartureTime()));
                passedDto.setFactArrivalTime(OperatorLocationUtils.getDateFromLocalDateTime(checkPointResult.getFactArrivalTime()));
                passedDto.setFactDepartureTime(OperatorLocationUtils.getDateFromLocalDateTime(checkPointResult.getFactDepartureTime()));
                passedDto.setArrivalLate(OperatorLocationUtils.beforeDate(checkPointResult.getArrivalTime(), checkPointResult.getFactArrivalTime()));
                passedDto.setDepartureLate(OperatorLocationUtils.beforeDate(checkPointResult.getDepartureTime(), checkPointResult.getFactDepartureTime()));
                passedDto.setPassed(OperatorLocationUtils.isCheckpointPassed(
                        checkPointResult.getFactArrivalTime(),
                        checkPointResult.getFactDepartureTime(),
                        checkPointResult.getDepartureTime()));
                result.add(passedDto);

            } else {

                CheckPointPassedDto passedDto = new CheckPointPassedDto();
                passedDto.setName(checkPoint.getName());
                passedDto.setAddress(checkPoint.getAddress());
                passedDto.setDescription(checkPoint.getDescription());
                passedDto.setArrivalTime(OperatorLocationUtils.getDateFromLocalDateTime(checkPoint.getArrivalTime()));
                passedDto.setDepartureTime(OperatorLocationUtils.getDateFromLocalDateTime(checkPoint.getDepartureTime()));
                passedDto.setArrivalLate(false);
                passedDto.setDepartureLate(false);
                passedDto.setPassed(false);
                result.add(passedDto);
            }
        }
        return result;
    }

    private List<RouteIntervalDto> getRouteIntervals(Route route, List<Location> locations, List<CheckPointResultDto> checkPointResults) {
        if (CollectionUtils.isEmpty(route.getCheckPoints())) {
            return Collections.emptyList();
        }
        List<RouteIntervalDto> intervals = new ArrayList<>();
        if (route.getCheckPoints().size() != 1) {
            for (int i = 0; i < route.getCheckPoints().size() - 1; i++) {
                CheckPoint startCheckpoint = route.getCheckPoints().get(i);
                CheckPoint endCheckpoint = route.getCheckPoints().get(i + 1);
                RouteIntervalDto routeInterval = new RouteIntervalDto();
                routeInterval.setStartName(startCheckpoint.getName());
                routeInterval.setEndName(endCheckpoint.getName());
                routeInterval.setFactStartTime(getFactStartTime(startCheckpoint, checkPointResults));
                routeInterval.setFactEndTime(getFactEndTime(endCheckpoint, checkPointResults));
                routeInterval.setStartTime(OperatorLocationUtils.getDateFromLocalDateTime(startCheckpoint.getDepartureTime()));
                routeInterval.setEndTime(OperatorLocationUtils.getDateFromLocalDateTime(endCheckpoint.getArrivalTime()));
                routeInterval.setPlanTimeMinutes(OperatorLocationUtils.calcTimeInMinutes(startCheckpoint.getDepartureTime(), endCheckpoint.getArrivalTime()));
                routeInterval.setFactTimeMinutes(getFactIntervalTime(getCheckpointResult(startCheckpoint, checkPointResults), getCheckpointResult(endCheckpoint, checkPointResults)));
                routeInterval.setArrivalLate(OperatorLocationUtils.beforeDate(routeInterval.getStartTime(), routeInterval.getFactStartTime()));
                routeInterval.setDepartureLate(OperatorLocationUtils.beforeDate(routeInterval.getEndTime(), routeInterval.getFactEndTime()));
                intervals.add(routeInterval);
            }
        }
        return intervals;
    }

    private Integer getFactIntervalTime(CheckPointResultDto start, CheckPointResultDto end) {
        if (start == null || end == null) {
            return null;
        }
        if (start.getFactDepartureTime() == null || end.getFactArrivalTime() == null) {
            return null;
        }
        return OperatorLocationUtils.calcTimeInMinutes(start.getFactDepartureTime(), end.getFactArrivalTime());
    }

    private Date getFactStartTime(CheckPoint checkPoint, List<CheckPointResultDto> checkPointResults) {
        CheckPointResultDto checkPointResult = getCheckpointResult(checkPoint, checkPointResults);
        if (checkPointResult != null) {
            return OperatorLocationUtils.getDateFromLocalDateTime(checkPointResult.getFactDepartureTime());
        } else {
            return null;
        }
    }

    private Date getFactEndTime(CheckPoint checkPoint, List<CheckPointResultDto> checkPointResults) {
        CheckPointResultDto checkPointResult = getCheckpointResult(checkPoint, checkPointResults);
        if (checkPointResult != null) {
            return OperatorLocationUtils.getDateFromLocalDateTime(checkPointResult.getFactArrivalTime());
        } else {
            return null;
        }
    }

    private CheckPointResultDto getCheckpointResult(CheckPoint checkPoint, List<CheckPointResultDto> checkPointResults) {
        if (CollectionUtils.isEmpty(checkPointResults)) {
            return null;
        }
        for (CheckPointResultDto checkPointResultDto : checkPointResults) {
            if (checkPoint.getId().equals(checkPointResultDto.getId())) {
                return checkPointResultDto;
            }
        }
        return null;
    }


}
