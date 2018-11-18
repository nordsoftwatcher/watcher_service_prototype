package ru.nord.siwatch.backend.services.locationmonitoring.api.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.api.dto.AbstractRecordDto;

@Getter @Setter
public class LocationRecordDto extends AbstractRecordDto
{
    // The latitude component of the device co-ordinate [-90.0 ~ 90.0] (degrees).
    private Double latitude;

    // The longitude component of the device co-ordinate[-180.0 ~ 180.0] (degrees).
    private Double longitude;

    // The altitude value.
    private Double altitude;

    // The device speed.
    private Double speed;

    // The device direction with respect to the north.
    private Double direction;

    // The accuracy in meters.
    private Double accuracy;
}
