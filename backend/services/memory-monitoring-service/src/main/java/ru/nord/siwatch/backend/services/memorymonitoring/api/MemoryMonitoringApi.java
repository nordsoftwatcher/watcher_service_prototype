package ru.nord.siwatch.backend.services.memorymonitoring.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import ru.nord.siwatch.backend.services.memorymonitoring.api.dto.MemoryRecordCreateDto;
import ru.nord.siwatch.backend.services.memorymonitoring.api.dto.MemoryRecordDto;
import ru.nord.siwatch.backend.services.memorymonitoring.api.dto.MemoryRecordSearchDto;
import ru.nord.siwatch.backend.services.memorymonitoring.entities.MemoryRecord;
import ru.nord.siwatch.backend.services.memorymonitoring.mapping.MemoryRecordMapper;
import ru.nord.siwatch.backend.services.memorymonitoring.repositories.MemoryRecordRepository;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Api(description = "MemoryMonitoring")
@RestController
@RequestMapping(value = ApiBase.PATH + MemoryMonitoringApi.PATH)
@Slf4j
public class MemoryMonitoringApi extends ApiBase {

    public static final String PATH = "memory_mon";

    @Autowired
    private MemoryRecordRepository memoryRecordRepository;

    @Autowired
    private MemoryRecordMapper memoryRecordMapper;

    @ApiOperation(value = "Создание записи в журнале")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemoryRecordDto> add(@RequestBody MemoryRecordCreateDto createDto) {
        ensureValid(createDto);

        MemoryRecord record = memoryRecordMapper.toMemoryRecord(createDto);
        record = memoryRecordRepository.saveAndFlush(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(memoryRecordMapper.toMemoryRecordDto(record));
    }

    @ApiOperation(value = "Поиск записей в журнале")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MemoryRecordDto> search(MemoryRecordSearchDto searchDto) {
        ensureValid(searchDto);

        final List<MemoryRecord> records = memoryRecordRepository.findAllByDeviceIdInInterval(
            searchDto.getDeviceId(),
            Date.from(searchDto.getFromTime().toInstant(ZoneOffset.UTC)),
            Date.from(searchDto.getToTime().toInstant(ZoneOffset.UTC)));

        return memoryRecordMapper.toMemoryRecordDtoList(records);
    }

}
