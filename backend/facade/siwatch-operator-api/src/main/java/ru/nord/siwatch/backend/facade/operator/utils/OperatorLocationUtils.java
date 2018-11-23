package ru.nord.siwatch.backend.facade.operator.utils;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.route.models.RoutePoint;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static java.lang.Math.*;

@Slf4j
public class OperatorLocationUtils {

    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;

    public static Integer calcFactTime(Pair<LocalDateTime, LocalDateTime> arrivalDepartureInfo) {
        if (arrivalDepartureInfo.getKey() == null || arrivalDepartureInfo.getValue() == null) {
            return null;
        }
        Date arrivalTime = Date.from(arrivalDepartureInfo.getKey().toInstant(ZoneOffset.UTC));
        Date departureTime = Date.from(arrivalDepartureInfo.getValue().toInstant(ZoneOffset.UTC));

        return toIntExact(abs(departureTime.getTime() - arrivalTime.getTime()) / MINUTE);

    }

    public static Pair<LocalDateTime, LocalDateTime> getArrivalAndDepartureTime(CheckPoint checkPoint, List<Location> locations) {
        LocalDateTime arrivalTime = null;
        LocalDateTime departureTime = null;
        for (Location location : locations) {
            double distance = distanceBetweenLocationAndCheckpoint(checkPoint, location);
            if (distance <= checkPoint.getRadius() && arrivalTime == null) {
               arrivalTime = location.getDeviceTime();
            } else {
                if (arrivalTime != null) {
                    departureTime = location.getDeviceTime();
                }
            }
        }
        if (arrivalTime != null) {
            return new Pair<>(arrivalTime, departureTime);
        }

        return null;
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
