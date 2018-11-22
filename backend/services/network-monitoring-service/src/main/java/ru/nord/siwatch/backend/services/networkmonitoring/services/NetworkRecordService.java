package ru.nord.siwatch.backend.services.networkmonitoring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.services.networkmonitoring.entities.NetworkRecord;
import ru.nord.siwatch.backend.services.networkmonitoring.repositories.NetworkRecordRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class NetworkRecordService {

    @Autowired
    private NetworkRecordRepository recordRepository;

    public List<NetworkRecord> findRecords(String deviceId, Date from, Date to) {
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
