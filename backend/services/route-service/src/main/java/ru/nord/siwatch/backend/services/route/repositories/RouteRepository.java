package ru.nord.siwatch.backend.services.route.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nord.siwatch.backend.services.route.entities.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
