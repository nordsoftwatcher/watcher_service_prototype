package ru.nord.siwatch.backend.services.locationmonitoring.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.services.locationmonitoring.api.dto.DtoMapper;
import ru.nord.siwatch.backend.services.locationmonitoring.api.dto.LocationRecordCreateDto;
import ru.nord.siwatch.backend.services.locationmonitoring.api.dto.LocationRecordDto;
import ru.nord.siwatch.backend.services.locationmonitoring.api.dto.LocationRecordSearchDto;
import ru.nord.siwatch.backend.services.locationmonitoring.entities.LocationRecord;
import ru.nord.siwatch.backend.services.locationmonitoring.repositories.LocationRecordRepository;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import ru.nord.siwatch.backend.services.locationmonitoring.services.LocationRecordService;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "LocationMonitoring")
@RestController
@RequestMapping(value = ApiBase.PATH + LocationMonitoringApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LocationMonitoringApi extends ApiBase {
    public static final String PATH = "locmon";

    @Autowired
    private DtoMapper mapper;

    @Autowired
    private LocationRecordRepository repository;

    @Autowired
    private LocationRecordService recordService;


    @ApiOperation(value = "Создание записи в журнале")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public LocationRecordDto add(@RequestBody LocationRecordCreateDto createDto) {
        ensureValid(createDto);
        LocationRecord record = mapper.createLocationRecord(createDto);
        return mapper.getFullBatteryLevelRecordDto(repository.saveAndFlush(record));
    }

    @ApiOperation(value = "Поиск записей в журнале")
    @GetMapping
    public List<LocationRecordDto> search(LocationRecordSearchDto searchDto) {
        ensureValid(searchDto);

        final List<LocationRecord> records = recordService.findRecords(
                searchDto.getDeviceId(),
                searchDto.getFromTime() != null ? Date.from(searchDto.getFromTime().toInstant(ZoneOffset.UTC)) : null,
                searchDto.getToTime() != null ? Date.from(searchDto.getToTime().toInstant(ZoneOffset.UTC)) : null
        );

        return records.stream().map(mapper::getBriefLocationRecordDto).collect(Collectors.toList());
    }

}
