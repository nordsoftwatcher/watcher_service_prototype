package ru.nord.siwatch.backend.services.supervisor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nord.siwatch.backend.services.supervisor.entities.Supervisor;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

}
