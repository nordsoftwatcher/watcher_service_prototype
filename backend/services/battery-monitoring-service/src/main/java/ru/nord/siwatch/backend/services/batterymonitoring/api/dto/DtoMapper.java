package ru.nord.siwatch.backend.services.batterymonitoring.api.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nord.siwatch.backend.services.batterymonitoring.entities.BatteryLevelRecord;

@Mapper
public interface DtoMapper
{
    BatteryLevelRecord createRecord(BatteryLevelRecordCreateDto dto);

    BatteryLevelRecordDto getBriefBatteryLevelRecordDto(BatteryLevelRecord record);

    BatteryLevelRecordDto getFullBatteryLevelRecordDto(BatteryLevelRecord record);
}
