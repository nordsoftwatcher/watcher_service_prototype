package ru.nord.siwatch.backend.facade.operator.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.CheckPointResultDto;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.LocationDto;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class DeviceLocationOutput {

    private List<LocationDto> locations;

    private List<CheckPointResultDto> checkpoints;

}
