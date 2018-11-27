package ru.nord.siwatch.backend.facade.operator.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.CheckPoint;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.route.models.RoutePoint;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.ArrivalDepartureInfo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static java.lang.Math.*;

@Slf4j
public class OperatorLocationUtils {

    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;

    public static Integer calcFactTime(ArrivalDepartureInfo arrivalDepartureInfo) {
        if (arrivalDepartureInfo.getArrivalTime() == null || arrivalDepartureInfo.getDepartureTime() == null) {
            return null;
        }
        Date arrivalTime = Date.from(arrivalDepartureInfo.getArrivalTime().toInstant(ZoneOffset.UTC));
        Date departureTime = Date.from(arrivalDepartureInfo.getDepartureTime().toInstant(ZoneOffset.UTC));

        return toIntExact(abs(departureTime.getTime() - arrivalTime.getTime()) / MINUTE);

    }

    public static Integer calcTimeInMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return null;
        }
        Date arrivalTime = Date.from(startTime.toInstant(ZoneOffset.UTC));
        Date departureTime = Date.from(endTime.toInstant(ZoneOffset.UTC));

        return toIntExact(abs(departureTime.getTime() - arrivalTime.getTime()) / MINUTE);

    }

    public static ArrivalDepartureInfo getArrivalAndDepartureTime(CheckPoint checkPoint, List<Location> locations) {
        if (CollectionUtils.isEmpty(locations)) {
            return null;
        }
        LocalDateTime arrivalTime = null;
        LocalDateTime departureTime = null;
        for (Location location : locations) {
            double distance = distanceBetweenLocationAndCheckpoint(checkPoint, location);
            if (distance <= checkPoint.getRadius()) {
                if (arrivalTime == null) {
                    arrivalTime = location.getDeviceTime();
                }
            } else {
                if (arrivalTime != null) {
                    departureTime = location.getDeviceTime();
                    break;
                }
            }
        }
        if (arrivalTime != null) {
            return new ArrivalDepartureInfo(arrivalTime, departureTime);
        }

        return null;
    }

    public static double distanceBetweenLocationAndCheckpoint(CheckPoint checkPoint, Location location) {
        return sqrt(
                pow(checkPoint.getLatitude() - location.getLatitude(), 2) +
                pow(checkPoint.getLongitude() - location.getLongitude(), 2)
        );
    }

    public static double distanceFromRoute(Route route, Location location) {
        if (CollectionUtils.isEmpty(route.getRoutePoints())) {
            return -1;
        }
        if (route.getRoutePoints().size() < 2) {
            return -1;
        }
        double distance = -1;
        for (int i = 0; i < route.getRoutePoints().size() - 1; i++) {
            double tempDistance = distanceFromRouteLine(
                    route.getRoutePoints().get(i),
                    route.getRoutePoints().get(i + 1),
                    location);
            if (distance < 0) {
                distance = tempDistance;
            } else {
                if (tempDistance < distance) {
                    distance = tempDistance;
                }
            }
        }
        return distance;
    }

    public static double distanceFromRouteLine(RoutePoint first, RoutePoint second, Location location) {
        double numerator = abs(
                (second.getLongitude() - first.getLongitude()) * location.getLatitude() -
                (second.getLatitude() - first.getLatitude()) * location.getLongitude() +
                (second.getLatitude() * first.getLongitude()) -
                (second.getLongitude() * first.getLatitude())
        );
        double denominator = sqrt(pow(second.getLongitude() - first.getLongitude(), 2) + pow(second.getLatitude() - first.getLatitude(), 2));

        if (denominator != 0) {
            return numerator / denominator;
        } else {
            return 0;
        }
    }

}
