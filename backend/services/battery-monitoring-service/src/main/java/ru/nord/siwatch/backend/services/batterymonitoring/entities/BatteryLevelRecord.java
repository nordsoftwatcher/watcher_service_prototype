package ru.nord.siwatch.backend.services.batterymonitoring.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity @Table(name = "battery_level_log")
@Getter @Setter
public class BatteryLevelRecord extends AbstractRecord
{
    @Column(name = "level", nullable = false)
    private Float level;
}
