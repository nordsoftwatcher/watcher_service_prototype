package ru.nord.siwatch.backend.services.networkmonitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nord.siwatch.backend.services.networkmonitoring.entities.NetworkRecord;

import java.util.Date;
import java.util.List;

public interface NetworkRecordRepository extends JpaRepository<NetworkRecord, Long> {

    @Query("SELECT r FROM NetworkRecord r WHERE r.deviceId=:deviceId " +
        "AND r.deviceTime>=:from AND r.deviceTime<=:to " +
        "ORDER BY r.deviceTime DESC")
    List<NetworkRecord> findAllByDeviceIdInInterval(@Param("deviceId") String deviceId, @Param("from") Date from, @Param("to") Date to);

}
