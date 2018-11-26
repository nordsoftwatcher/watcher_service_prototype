package ru.nord.siwatch.backend.services.events.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class CreateEventInput {

    @NotNull(message = "Supervisor id can't be null")
    private Long supervisorId;

    @ApiModelProperty(notes = "Время фиксации показания на устройстве", example = "2018-11-20T15:00:00")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deviceTime;

    @NotBlank(message = "Event type can't be null")
    private String eventType;

    @NotBlank(message = "Event value can't be null")
    private String eventValue;

    private Double latitude;

    private Double longitude;

    @AssertTrue(message = "Latitude and longitude must be both empty or not at the same time")
    private boolean hasBothCoordinateValues() {
        return ((latitude == null && longitude == null) || (latitude != null && longitude != null));
    }

}
