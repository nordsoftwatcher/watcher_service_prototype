package ru.nord.siwatch.backend.facade.operator.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class CreateRouteInput {

    private String name;

    @NotNull(message = "Supervisor id can't be empty")
    private Long supervisorId;

    @ApiModelProperty(example = "2018-11-20T15:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @ApiModelProperty(example = "2018-11-20T15:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    @Valid
    @NotEmpty(message = "Route has to contain at least one point")
    private List<CreateRoutePointInput> routePoints;

    @Valid
    @NotEmpty(message = "Route has to contain at least one check point")
    private List<CreateCheckPointInput> checkPoints;

}
