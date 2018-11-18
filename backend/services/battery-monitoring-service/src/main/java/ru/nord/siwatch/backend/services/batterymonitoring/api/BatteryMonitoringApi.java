package ru.nord.siwatch.backend.services.batterymonitoring.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import ru.nord.siwatch.backend.services.common.api.ApiBase;

import java.sql.Date;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "BatteryMonitoring")
@RestController
@RequestMapping(value = ApiBase.PATH + BatteryMonitoringApi.PATH)
@Slf4j
public class BatteryMonitoringApi extends ApiBase
{
    public static final String PATH = "battmon";

    private final DtoMapper mapper;
    private final BatteryLevelRecordRepository repository;

    public BatteryMonitoringApi(BatteryLevelRecordRepository repository, DtoMapper mapper)
    {
        this.repository = repository;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Создание записи в журнале")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatteryLevelRecordDto> add(@RequestBody BatteryLevelRecordCreateDto createDto)
    {
        ensureValid(createDto);
        BatteryLevelRecord record = mapper.createRecord(createDto);
        record = repository.saveAndFlush(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.getFullBatteryLevelRecordDto(record));
    }

    @ApiOperation(value = "Поиск записей в журнале")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BatteryLevelRecordDto> search(BatteryLevelRecordSearchDto searchDto)
    {
        ensureValid(searchDto);

        final List<BatteryLevelRecord> records = repository.findAllByDeviceIdInInterval(
            searchDto.getDeviceId(),
            Date.from(searchDto.getFromTime().toInstant(ZoneOffset.UTC)),
            Date.from(searchDto.getToTime().toInstant(ZoneOffset.UTC)));

        return records.stream().map(mapper::getBriefBatteryLevelRecordDto).collect(Collectors.toList());
    }

}
