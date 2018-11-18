package ru.nord.siwatch.backend.services.heartratemonitoring.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity @Table(name = "heart_rate_log")
@Getter @Setter
public class HeartRateRecord extends AbstractRecord
{
    @Column(name = "rate", nullable = false)
    private Float rate;
}
