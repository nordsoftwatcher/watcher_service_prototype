package ru.nord.siwatch.backend.services.networkmonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordDto;

@Getter @Setter
public class NetworkRecordDto extends AbstractRecordDto {

    private Boolean available;
}
