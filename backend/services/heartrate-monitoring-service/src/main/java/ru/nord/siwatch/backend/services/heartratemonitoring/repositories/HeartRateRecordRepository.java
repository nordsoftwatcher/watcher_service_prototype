package ru.nord.siwatch.backend.services.heartratemonitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nord.siwatch.backend.services.heartratemonitoring.entities.HeartRateRecord;

import java.util.Date;
import java.util.List;

public interface HeartRateRecordRepository extends JpaRepository<HeartRateRecord, Long>
{
    @Query("SELECT r FROM HeartRateRecord r WHERE r.deviceId=?1 AND r.deviceTime>=?2 AND r.deviceTime<=?3 ORDER BY r.deviceTime DESC")
    List<HeartRateRecord> findAllByDeviceIdInInterval(String deviceId, Date from, Date to);

    @Query("SELECT r FROM HeartRateRecord r WHERE r.deviceId=?1 AND r.deviceTime>=?2 ORDER BY r.deviceTime DESC")
    List<HeartRateRecord> findAllByDeviceIdAndFromDate(String deviceId, Date from);

    @Query("SELECT r FROM HeartRateRecord r WHERE r.deviceId=?1 AND r.deviceTime<=?2 ORDER BY r.deviceTime DESC")
    List<HeartRateRecord> findAllByDeviceIdAndToDate(String deviceId, Date to);

    @Query("SELECT r FROM HeartRateRecord r WHERE r.deviceId=?1 ORDER BY r.deviceTime DESC")
    List<HeartRateRecord> findAllByDeviceId(String deviceId);
}
