package ru.nord.siwatch.backend.facade.operator.api.v1.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @ToString
@ApiModel(description = "Ошибка")
public class Fault
{
    @ApiModelProperty(notes = "Код ошибки", example = "E000", position = 0)
    private String code;
    @ApiModelProperty(notes = "Сообщение", example = "Внутренняя ошибка", position = 1)
    private String message;
    @ApiModelProperty(notes = "Детали ошибки", position = 2)
    private String details;
}
