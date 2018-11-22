package ru.nord.siwatch.backend.facade.operator.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.route.models.RoutePoint;

import static java.lang.Math.*;

@Slf4j
public class LocationUtils {

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
