package ru.nord.siwatch.backend.services.supervisor.mapping;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.services.supervisor.api.dto.SupervisorDto;
import ru.nord.siwatch.backend.services.supervisor.entities.Supervisor;

@Mapper
public interface SupervisorMapper {

    SupervisorDto toSupervisorDto(Supervisor supervisor);

}
