package ru.nord.siwatch.backend.services.locationmonitoring.api.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nord.siwatch.backend.services.locationmonitoring.entities.LocationRecord;

@Mapper
public interface DtoMapper
{
    LocationRecord createLocationRecord(LocationRecordCreateDto dto);

    @Mapping(target = "deviceId", ignore = true)
    LocationRecordDto getBriefLocationRecordDto(LocationRecord record);

    LocationRecordDto getFullBatteryLevelRecordDto(LocationRecord record);
}
