package ru.nord.siwatch.backend.services.route.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nord.siwatch.backend.services.route.entities.CheckPoint;

public interface CheckPointRepository extends JpaRepository<CheckPoint, Long> {

}
