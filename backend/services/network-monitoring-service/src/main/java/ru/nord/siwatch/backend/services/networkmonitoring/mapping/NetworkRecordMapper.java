package ru.nord.siwatch.backend.services.networkmonitoring.mapping;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.services.networkmonitoring.api.dto.NetworkRecordCreateDto;
import ru.nord.siwatch.backend.services.networkmonitoring.api.dto.NetworkRecordDto;
import ru.nord.siwatch.backend.services.networkmonitoring.entities.NetworkRecord;

import java.util.List;

@Mapper
public interface NetworkRecordMapper {

    NetworkRecord toNetworkRecord(NetworkRecordCreateDto createDto);

    NetworkRecordDto toNetworkRecordDto(NetworkRecord networkRecord);

    List<NetworkRecordDto> toNetworkRecordDtoList(List<NetworkRecord> networkRecords);

}
