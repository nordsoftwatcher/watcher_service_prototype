package ru.nord.siwatch.backend.services.batterymonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordDto;

@Getter @Setter
public class BatteryLevelRecordDto extends AbstractRecordDto
{
    private float level;
}
