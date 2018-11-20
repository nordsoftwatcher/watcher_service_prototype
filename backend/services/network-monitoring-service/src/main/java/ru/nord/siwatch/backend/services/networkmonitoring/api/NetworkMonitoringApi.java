package ru.nord.siwatch.backend.services.networkmonitoring.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import ru.nord.siwatch.backend.services.networkmonitoring.api.dto.NetworkRecordCreateDto;
import ru.nord.siwatch.backend.services.networkmonitoring.api.dto.NetworkRecordDto;
import ru.nord.siwatch.backend.services.networkmonitoring.api.dto.NetworkRecordSearchDto;
import ru.nord.siwatch.backend.services.networkmonitoring.entities.NetworkRecord;
import ru.nord.siwatch.backend.services.networkmonitoring.mapping.NetworkRecordMapper;
import ru.nord.siwatch.backend.services.networkmonitoring.repositories.NetworkRecordRepository;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Api(description = "NetworkMonitoring")
@RestController
@RequestMapping(value = ApiBase.PATH + NetworkMonitoringApi.PATH)
@Slf4j
public class NetworkMonitoringApi extends ApiBase {

    public static final String PATH = "network_mon";

    @Autowired
    private NetworkRecordMapper networkRecordMapper;

    @Autowired
    private NetworkRecordRepository networkRecordRepository;

    @ApiOperation(value = "Создание записи в журнале")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NetworkRecordDto> add(@RequestBody NetworkRecordCreateDto createDto) {
        ensureValid(createDto);

        NetworkRecord record = networkRecordMapper.toNetworkRecord(createDto);
        record = networkRecordRepository.saveAndFlush(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(networkRecordMapper.toNetworkRecordDto(record));
    }

    @ApiOperation(value = "Поиск записей в журнале")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NetworkRecordDto> search(NetworkRecordSearchDto searchDto) {
        ensureValid(searchDto);

        final List<NetworkRecord> records = networkRecordRepository.findAllByDeviceIdInInterval(
            searchDto.getDeviceId(),
            Date.from(searchDto.getFromTime().toInstant(ZoneOffset.UTC)),
            Date.from(searchDto.getToTime().toInstant(ZoneOffset.UTC)));

        return networkRecordMapper.toNetworkRecordDtoList(records);
    }

}
