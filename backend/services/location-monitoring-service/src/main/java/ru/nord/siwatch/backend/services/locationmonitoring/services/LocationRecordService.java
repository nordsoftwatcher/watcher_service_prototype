package ru.nord.siwatch.backend.services.locationmonitoring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.services.locationmonitoring.entities.LocationRecord;
import ru.nord.siwatch.backend.services.locationmonitoring.repositories.LocationRecordRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LocationRecordService {

    @Autowired
    private LocationRecordRepository recordRepository;

    public List<LocationRecord> findRecords(String deviceId, Date from, Date to) {
        if (from != null && to != null) {
            return recordRepository.findAllByDeviceIdInInterval(deviceId, from, to);
        }
        if (from != null && to == null) {
            return recordRepository.findAllByDeviceIdAndFromDate(deviceId, from);
        }
        if (from == null && to != null) {
            return recordRepository.findAllByDeviceIdAndToDate(deviceId, to);
        }
        return recordRepository.findAllByDeviceId(deviceId);
    }

}
