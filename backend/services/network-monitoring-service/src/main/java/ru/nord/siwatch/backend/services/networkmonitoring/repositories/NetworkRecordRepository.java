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

    @Query("SELECT r FROM NetworkRecord r WHERE r.deviceId=?1 AND r.deviceTime>=?2 ORDER BY r.deviceTime DESC")
    List<NetworkRecord> findAllByDeviceIdAndFromDate(String deviceId, Date from);

    @Query("SELECT r FROM NetworkRecord r WHERE r.deviceId=?1 AND r.deviceTime<=?2 ORDER BY r.deviceTime DESC")
    List<NetworkRecord> findAllByDeviceIdAndToDate(String deviceId, Date to);

    @Query("SELECT r FROM NetworkRecord r WHERE r.deviceId=?1 ORDER BY r.deviceTime DESC")
    List<NetworkRecord> findAllByDeviceId(String deviceId);
}
