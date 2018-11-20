package ru.nord.siwatch.backend.services.common.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractEntityDto {

    @ApiModelProperty(notes = "ID объекта")
    private Long id;
}
