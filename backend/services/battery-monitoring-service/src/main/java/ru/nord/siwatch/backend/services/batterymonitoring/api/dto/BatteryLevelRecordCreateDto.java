package ru.nord.siwatch.backend.services.batterymonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordCreateDto;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class BatteryLevelRecordCreateDto extends AbstractRecordCreateDto
{
    @NotNull
    private Float level;
}
