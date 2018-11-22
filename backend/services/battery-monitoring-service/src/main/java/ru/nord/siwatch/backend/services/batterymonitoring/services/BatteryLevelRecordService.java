package ru.nord.siwatch.backend.services.batterymonitoring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.services.batterymonitoring.entities.BatteryLevelRecord;
import ru.nord.siwatch.backend.services.batterymonitoring.repositories.BatteryLevelRecordRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BatteryLevelRecordService {

    @Autowired
    private BatteryLevelRecordRepository recordRepository;

    public List<BatteryLevelRecord> findRecords(String deviceId, Date from, Date to) {
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
