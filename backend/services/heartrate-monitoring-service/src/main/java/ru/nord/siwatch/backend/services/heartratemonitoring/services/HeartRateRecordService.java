package ru.nord.siwatch.backend.services.heartratemonitoring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.services.heartratemonitoring.entities.HeartRateRecord;
import ru.nord.siwatch.backend.services.heartratemonitoring.repositories.HeartRateRecordRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class HeartRateRecordService {

    @Autowired
    private HeartRateRecordRepository recordRepository;

    public List<HeartRateRecord> findRecords(String deviceId, Date from, Date to) {
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
