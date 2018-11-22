package ru.nord.siwatch.backend.services.heartratemonitoring.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.services.heartratemonitoring.api.dto.DtoMapper;
import ru.nord.siwatch.backend.services.heartratemonitoring.api.dto.HeartRateRecordCreateDto;
import ru.nord.siwatch.backend.services.heartratemonitoring.api.dto.HeartRateRecordDto;
import ru.nord.siwatch.backend.services.heartratemonitoring.api.dto.HeartRateRecordSearchDto;
import ru.nord.siwatch.backend.services.heartratemonitoring.entities.HeartRateRecord;
import ru.nord.siwatch.backend.services.heartratemonitoring.repositories.HeartRateRecordRepository;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import ru.nord.siwatch.backend.services.heartratemonitoring.services.HeartRateRecordService;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "HeartRateMonitoring")
@RestController
@RequestMapping(value = ApiBase.PATH + HeartRateMonitoringApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class HeartRateMonitoringApi extends ApiBase {
    public static final String PATH = "hrmon";

    @Autowired
    private DtoMapper mapper;

    @Autowired
    private HeartRateRecordRepository repository;

    @Autowired
    private HeartRateRecordService recordService;


    @ApiOperation(value = "Создание записи в журнале")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public HeartRateRecordDto add(@RequestBody HeartRateRecordCreateDto createDto) {
        ensureValid(createDto);
        HeartRateRecord record = mapper.createRecord(createDto);
        return mapper.getFullHeartRateRecordDto(repository.saveAndFlush(record));
    }

    @ApiOperation(value = "Поиск записей в журнале")
    @GetMapping
    public List<HeartRateRecordDto> search(HeartRateRecordSearchDto searchDto) {
        ensureValid(searchDto);

        final List<HeartRateRecord> records = recordService.findRecords(
                searchDto.getDeviceId(),
                searchDto.getFromTime() != null ? Date.from(searchDto.getFromTime().toInstant(ZoneOffset.UTC)) : null,
                searchDto.getToTime() != null ? Date.from(searchDto.getToTime().toInstant(ZoneOffset.UTC)) : null
        );

        return records.stream().map(mapper::getBriefHeartRateRecordDto).collect(Collectors.toList());
    }

}
