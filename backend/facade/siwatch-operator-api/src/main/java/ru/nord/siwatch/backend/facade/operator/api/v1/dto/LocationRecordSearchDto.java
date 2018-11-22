package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocationRecordSearchDto extends AbstractRecordSearchDto {

    @ApiModelProperty(notes = "ID маршрута", example = "TEST_DEVICE", position = 0)
    private Long routeId;

}
