package ru.nord.siwatch.backend.services.batterymonitoring.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.services.batterymonitoring.api.dto.BatteryLevelRecordDto;
import ru.nord.siwatch.backend.services.batterymonitoring.api.dto.BatteryLevelRecordSearchDto;
import ru.nord.siwatch.backend.services.batterymonitoring.api.dto.BatteryLevelRecordCreateDto;
import ru.nord.siwatch.backend.services.batterymonitoring.api.dto.DtoMapper;
import ru.nord.siwatch.backend.services.batterymonitoring.entities.BatteryLevelRecord;
import ru.nord.siwatch.backend.services.batterymonitoring.repositories.BatteryLevelRecordRepository;
import ru.nord.siwatch.backend.services.batterymonitoring.services.BatteryLevelRecordService;
import ru.nord.siwatch.backend.services.common.api.ApiBase;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "BatteryMonitoring")
@RestController
@RequestMapping(value = ApiBase.PATH + BatteryMonitoringApi.PATH)
@Slf4j
public class BatteryMonitoringApi extends ApiBase {
    public static final String PATH = "battmon";

    @Autowired
    private DtoMapper mapper;

    @Autowired
    private BatteryLevelRecordRepository repository;

    @Autowired
    private BatteryLevelRecordService recordService;


    @ApiOperation(value = "Создание записи в журнале")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatteryLevelRecordDto> add(@RequestBody BatteryLevelRecordCreateDto createDto) {
        ensureValid(createDto);
        BatteryLevelRecord record = mapper.createRecord(createDto);
        record = repository.saveAndFlush(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.getFullBatteryLevelRecordDto(record));
    }

    @ApiOperation(value = "Поиск записей в журнале")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BatteryLevelRecordDto> search(BatteryLevelRecordSearchDto searchDto) {
        ensureValid(searchDto);

        final List<BatteryLevelRecord> records = recordService.findRecords(
                searchDto.getDeviceId(),
                searchDto.getFromTime() != null ? Date.from(searchDto.getFromTime().toInstant(ZoneOffset.UTC)) : null,
                searchDto.getToTime() != null ? Date.from(searchDto.getToTime().toInstant(ZoneOffset.UTC)) : null
        );

        return records.stream().map(mapper::getBriefBatteryLevelRecordDto).collect(Collectors.toList());
    }

}
