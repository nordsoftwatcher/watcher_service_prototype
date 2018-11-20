package ru.nord.siwatch.backend.services.networkmonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordCreateDto;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class NetworkRecordCreateDto extends AbstractRecordCreateDto {

    @NotNull(message = "Network available status can't be null")
    private Boolean available;

}
