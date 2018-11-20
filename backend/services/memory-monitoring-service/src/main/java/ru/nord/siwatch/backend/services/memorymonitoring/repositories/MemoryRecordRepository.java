package ru.nord.siwatch.backend.services.memorymonitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nord.siwatch.backend.services.memorymonitoring.entities.MemoryRecord;

import java.util.Date;
import java.util.List;

public interface MemoryRecordRepository extends JpaRepository<MemoryRecord, Long> {

    @Query("SELECT r FROM MemoryRecord r WHERE r.deviceId=:deviceId " +
        "AND r.deviceTime>=:from AND r.deviceTime<=:to " +
        "ORDER BY r.deviceTime DESC")
    List<MemoryRecord> findAllByDeviceIdInInterval(@Param("deviceId") String deviceId, @Param("from") Date from, @Param("to") Date to);

}
