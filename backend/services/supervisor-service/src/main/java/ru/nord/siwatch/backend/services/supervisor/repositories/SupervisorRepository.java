package ru.nord.siwatch.backend.services.supervisor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nord.siwatch.backend.services.supervisor.entities.Supervisor;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

    @Query("SELECT supervisor FROM Supervisor as supervisor WHERE supervisor.id = :supervisorId")
    Supervisor getSupervisorById(@Param("supervisorId") Long supervisorId);

    @Query("SELECT supervisor FROM Supervisor as supervisor WHERE supervisor.deviceId = :deviceId")
    Supervisor getSupervisorByDeviceId(@Param("deviceId") String deviceId);
}
