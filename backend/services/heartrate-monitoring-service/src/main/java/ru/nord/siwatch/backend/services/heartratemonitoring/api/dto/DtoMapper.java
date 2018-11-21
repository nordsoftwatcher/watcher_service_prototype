package ru.nord.siwatch.backend.services.heartratemonitoring.api.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nord.siwatch.backend.services.heartratemonitoring.entities.HeartRateRecord;

@Mapper
public interface DtoMapper
{
    HeartRateRecord createRecord(HeartRateRecordCreateDto dto);

    HeartRateRecordDto getBriefHeartRateRecordDto(HeartRateRecord record);

    HeartRateRecordDto getFullHeartRateRecordDto(HeartRateRecord record);
}
