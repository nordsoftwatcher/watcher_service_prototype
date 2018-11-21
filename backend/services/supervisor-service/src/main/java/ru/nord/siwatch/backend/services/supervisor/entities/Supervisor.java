package ru.nord.siwatch.backend.services.supervisor.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "supervisor")
@Getter
@Setter
public class Supervisor extends AbstractEntity {

    @Column(name = "device_id", nullable = false, length = 32, unique = true)
    private String deviceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

}
