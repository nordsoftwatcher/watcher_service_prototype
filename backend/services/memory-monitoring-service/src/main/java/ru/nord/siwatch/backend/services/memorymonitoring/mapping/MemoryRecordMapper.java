package ru.nord.siwatch.backend.services.memorymonitoring.mapping;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.services.memorymonitoring.api.dto.MemoryRecordCreateDto;
import ru.nord.siwatch.backend.services.memorymonitoring.api.dto.MemoryRecordDto;
import ru.nord.siwatch.backend.services.memorymonitoring.entities.MemoryRecord;

import java.util.List;

@Mapper
public interface MemoryRecordMapper {

    MemoryRecord toMemoryRecord(MemoryRecordCreateDto memoryRecordCreateDto);

    MemoryRecordDto toMemoryRecordDto(MemoryRecord memoryRecord);

    List<MemoryRecordDto> toMemoryRecordDtoList(List<MemoryRecord> memoryRecords);

}
