package ru.nord.siwatch.backend.services.batterymonitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nord.siwatch.backend.services.batterymonitoring.entities.BatteryLevelRecord;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface BatteryLevelRecordRepository extends JpaRepository<BatteryLevelRecord, Long>
{
    @Query("SELECT r FROM BatteryLevelRecord r WHERE r.deviceId=?1 AND r.deviceTime>=?2 AND r.deviceTime<=?3 ORDER BY r.deviceTime DESC")
    List<BatteryLevelRecord> findAllByDeviceIdInInterval(String deviceId, Date from, Date to);

    @Query("SELECT r FROM BatteryLevelRecord r WHERE r.deviceId=?1 AND r.deviceTime>=?2 ORDER BY r.deviceTime DESC")
    List<BatteryLevelRecord> findAllByDeviceIdAndFromDate(String deviceId, Date from);

    @Query("SELECT r FROM BatteryLevelRecord r WHERE r.deviceId=?1 AND r.deviceTime<=?2 ORDER BY r.deviceTime DESC")
    List<BatteryLevelRecord> findAllByDeviceIdAndToDate(String deviceId, Date to);

    @Query("SELECT r FROM BatteryLevelRecord r WHERE r.deviceId=?1 ORDER BY r.deviceTime DESC")
    List<BatteryLevelRecord> findAllByDeviceId(String deviceId);
}
