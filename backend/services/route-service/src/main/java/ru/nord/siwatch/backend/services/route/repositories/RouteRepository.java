package ru.nord.siwatch.backend.services.route.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nord.siwatch.backend.services.route.entities.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT DISTINCT route FROM Route as route " +
        "LEFT JOIN FETCH route.routePoints " +
        "LEFT JOIN FETCH route.checkPoints " +
        "WHERE route.id = :routeId")
    Route getRouteWithPoints(@Param("routeId") Long routeId);
}
