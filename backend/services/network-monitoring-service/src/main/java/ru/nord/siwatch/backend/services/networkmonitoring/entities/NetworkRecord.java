package ru.nord.siwatch.backend.services.networkmonitoring.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "network_record_log")
@Getter
@Setter
public class NetworkRecord extends AbstractRecord {

    @Column(name = "available", nullable = false)
    private Boolean available;

}
