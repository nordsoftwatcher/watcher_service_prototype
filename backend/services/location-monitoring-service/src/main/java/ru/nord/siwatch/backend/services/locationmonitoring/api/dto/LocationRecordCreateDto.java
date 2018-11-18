package ru.nord.siwatch.backend.services.locationmonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordCreateDto;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class LocationRecordCreateDto extends AbstractRecordCreateDto
{
    // The latitude component of the device co-ordinate [-90.0 ~ 90.0] (degrees).
    @NotNull
    private Double latitude;

    // The longitude component of the device co-ordinate[-180.0 ~ 180.0] (degrees).
    @NotNull
    private Double longitude;

    // The altitude value.
    @NotNull
    private Double altitude;

    // The device speed.
    //@NotNull
    private Double speed;

    // The device direction with respect to the north.
    //@NotNull
    private Double direction;

    // The accuracy in meters.
    @NotNull
    private Double accuracy;
}
