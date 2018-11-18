package ru.nord.siwatch.backend.services.heartratemonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordCreateDto;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class HeartRateRecordCreateDto extends AbstractRecordCreateDto
{
    @NotNull
    private Float rate;
}
