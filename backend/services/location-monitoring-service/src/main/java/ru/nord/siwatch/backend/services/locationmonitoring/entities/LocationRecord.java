package ru.nord.siwatch.backend.services.locationmonitoring.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity @Table(name = "location_log")
@Getter @Setter
public class LocationRecord extends AbstractRecord
{
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "altitude")
    private Double altitude;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "direction")
    private Double direction;

    @Column(name = "accuracy")
    private Double accuracy;
}
