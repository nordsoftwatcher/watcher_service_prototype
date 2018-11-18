package ru.nord.siwatch.backend.services.batterymonitoring.api.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nord.siwatch.backend.services.batterymonitoring.entities.BatteryLevelRecord;

@Mapper
public interface DtoMapper
{
    BatteryLevelRecord createRecord(BatteryLevelRecordCreateDto dto);

    @Mapping(target = "deviceId", ignore = true)
    BatteryLevelRecordDto getBriefBatteryLevelRecordDto(BatteryLevelRecord record);

    BatteryLevelRecordDto getFullBatteryLevelRecordDto(BatteryLevelRecord record);
}
