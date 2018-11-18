package ru.nord.siwatch.backend.services.locationmonitoring.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.services.locationmonitoring.api.dto.DtoMapper;
import ru.nord.siwatch.backend.services.locationmonitoring.api.dto.LocationRecordCreateDto;
import ru.nord.siwatch.backend.services.locationmonitoring.api.dto.LocationRecordDto;
import ru.nord.siwatch.backend.services.locationmonitoring.api.dto.LocationRecordSearchDto;
import ru.nord.siwatch.backend.services.locationmonitoring.entities.LocationRecord;
import ru.nord.siwatch.backend.services.locationmonitoring.repositories.LocationRecordRepository;
import ru.nord.siwatch.backend.services.common.api.ApiBase;

import java.sql.Date;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "LocationMonitoring")
@RestController
@RequestMapping(value = ApiBase.PATH + LocationMonitoringApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LocationMonitoringApi extends ApiBase
{
    public static final String PATH = "locmon";

    private final DtoMapper mapper;
    private final LocationRecordRepository repository;

    public LocationMonitoringApi(LocationRecordRepository repository, DtoMapper mapper)
    {
        this.repository = repository;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Создание записи в журнале")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public LocationRecordDto add(@RequestBody LocationRecordCreateDto createDto)
    {
        ensureValid(createDto);
        LocationRecord record = mapper.createLocationRecord(createDto);
        return mapper.getFullBatteryLevelRecordDto(repository.saveAndFlush(record));
    }

    @ApiOperation(value = "Поиск записей в журнале")
    @GetMapping
    public List<LocationRecordDto> search(LocationRecordSearchDto searchDto)
    {
        ensureValid(searchDto);

        final List<LocationRecord> records = repository.findAllByDeviceIdInInterval(
            searchDto.getDeviceId(),
            Date.from(searchDto.getFromTime().toInstant(ZoneOffset.UTC)),
            Date.from(searchDto.getToTime().toInstant(ZoneOffset.UTC)));

        return records.stream().map(mapper::getBriefLocationRecordDto).collect(Collectors.toList());
    }

}
