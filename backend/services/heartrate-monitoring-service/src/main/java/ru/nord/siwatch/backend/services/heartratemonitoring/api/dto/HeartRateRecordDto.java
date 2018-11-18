package ru.nord.siwatch.backend.services.heartratemonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordDto;

@Getter @Setter
public class HeartRateRecordDto extends AbstractRecordDto
{
    private float rate;
}
