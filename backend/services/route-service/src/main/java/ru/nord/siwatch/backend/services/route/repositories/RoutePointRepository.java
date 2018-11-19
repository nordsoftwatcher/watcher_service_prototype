package ru.nord.siwatch.backend.services.route.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nord.siwatch.backend.services.route.entities.RoutePoint;

public interface RoutePointRepository extends JpaRepository<RoutePoint, Long> {
}
