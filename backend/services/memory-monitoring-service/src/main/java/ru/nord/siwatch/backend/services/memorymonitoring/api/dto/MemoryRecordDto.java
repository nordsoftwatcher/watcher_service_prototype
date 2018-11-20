package ru.nord.siwatch.backend.services.memorymonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordDto;

@Getter @Setter
public class MemoryRecordDto extends AbstractRecordDto {

    private Long freeSystemMemory;

    private Long freeStorageMemory;

}
