package ru.nord.siwatch.backend.services.events.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractEntity;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event")
@Getter
@Setter
public class Event extends AbstractEntity {

    @Column(name = "supervisor_id", nullable = false)
    private Long supervisorId;
    
    @Column(name = "record_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.NONE)
    private Date recordTime;

    @Column(name = "name", nullable = false)
    private String name;


    @PrePersist
    protected void onBeforeInsert() {
        recordTime = new Date();
    }

}
