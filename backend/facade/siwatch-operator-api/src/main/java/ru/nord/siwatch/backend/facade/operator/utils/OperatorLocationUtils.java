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
    private static final long HOUR = MINUTE * 60;

    public static boolean isCheckpointPassed(LocalDateTime factArrivalTime, LocalDateTime factDepartureTime, LocalDateTime departureTime) {
        if (factArrivalTime == null) {
            return false;
        }
        if (factArrivalTime != null && factDepartureTime != null) {
            return true;
        }
        if (factDepartureTime == null && departureTime == null) {
            return true;
        }
        return false;
    }

    public static boolean beforeDate(LocalDateTime first, LocalDateTime second) {
        if (first == null || second == null) {
            return false;
        }
        return beforeDate(getDateFromLocalDateTime(first), getDateFromLocalDateTime(second));
    }

    public static boolean beforeDate(Date first, Date second) {
        if (first == null || second == null) {
            return false;
        }
        return first.before(second);
    }

    public static Date getDateFromLocalDateTime(LocalDateTime localDateTime) {
        Date result = localDateTime != null ?  Date.from(localDateTime.toInstant(ZoneOffset.UTC)) : null;
        if (result != null) {
            return new Date(result.getTime() + (3 * HOUR)); //TODO
        } else {
            return null;
        }
    }

    public static Integer calcFactTime(ArrivalDepartureInfo arrivalDepartureInfo) {
        if (arrivalDepartureInfo.getArrivalTime() == null || arrivalDepartureInfo.getDepartureTime() == null) {
            return 0;
        }
        Date arrivalTime = Date.from(arrivalDepartureInfo.getArrivalTime().toInstant(ZoneOffset.UTC));
        Date departureTime = Date.from(arrivalDepartureInfo.getDepartureTime().toInstant(ZoneOffset.UTC));

        return toIntExact(abs(departureTime.getTime() - arrivalTime.getTime()) / MINUTE);

    }

    public static Integer calcTimeInMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return 0;
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
        return getDistance(first.getLatitude(), first.getLongitude(), second.getLatitude(), second.getLongitude(), location.getLatitude(), location.getLongitude());
    }

    private static double getDistance(double ax, double ay, double bx, double by, double x, double y) {
        if ((ax == x && ay == y) || (bx == x && by == y)) return 0;

        double AB = getDistanceBetweenPoints(ax, ay, bx, by);
        double AC = getDistanceBetweenPoints(ax, ay, x, y);

        if (AB == 0) return AC;

        double BC = getDistanceBetweenPoints(bx, by, x, y);

        if (isObtuseAngle(AC, BC, AB)) return BC;
        if (isObtuseAngle(BC, AC, AB)) return AC;

        double p = (AC + BC + AB) / 2;
        return 2 * sqrt(p * (p - AB) * (p - BC) * (p - AC)) / AB;
    }

    private static double getDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private static boolean isObtuseAngle(double oppositeLine, double a, double b) {
        double cos = (a * a + b * b - oppositeLine * oppositeLine) / (2 * a * b);
        return cos < 0;
    }

}
