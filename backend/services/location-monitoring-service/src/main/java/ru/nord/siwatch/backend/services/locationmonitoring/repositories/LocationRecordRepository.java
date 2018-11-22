package ru.nord.siwatch.backend.services.locationmonitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nord.siwatch.backend.services.locationmonitoring.entities.LocationRecord;

import java.util.Date;
import java.util.List;

public interface LocationRecordRepository extends JpaRepository<LocationRecord, Long>
{
    @Query("SELECT r FROM LocationRecord r WHERE r.deviceId=?1 AND r.deviceTime>=?2 AND r.deviceTime<=?3 ORDER BY r.deviceTime DESC")
    List<LocationRecord> findAllByDeviceIdInInterval(String deviceId, Date from, Date to);

    @Query("SELECT r FROM LocationRecord r WHERE r.deviceId=?1 AND r.deviceTime>=?2 ORDER BY r.deviceTime DESC")
    List<LocationRecord> findAllByDeviceIdAndFromDate(String deviceId, Date from);

    @Query("SELECT r FROM LocationRecord r WHERE r.deviceId=?1 AND r.deviceTime<=?2 ORDER BY r.deviceTime DESC")
    List<LocationRecord> findAllByDeviceIdAndToDate(String deviceId, Date to);

    @Query("SELECT r FROM LocationRecord r WHERE r.deviceId=?1 ORDER BY r.deviceTime DESC")
    List<LocationRecord> findAllByDeviceId(String deviceId);

}
