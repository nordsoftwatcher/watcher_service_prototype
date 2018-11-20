package ru.nord.siwatch.backend.services.memorymonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordCreateDto;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemoryRecordCreateDto extends AbstractRecordCreateDto {

    @NotNull(message = "Free system memory can't be null")
    private Long freeSystemMemory;

    @NotNull(message = "Free storage memory can't be null")
    private Long freeStorageMemory;

}
