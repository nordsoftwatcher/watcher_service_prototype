package ru.nord.siwatch.backend.services.events.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractEntity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "event")
@Getter
@Setter
public class Event extends AbstractEntity {

    @Column(name = "supervisor_id", nullable = false)
    private Long supervisorId;

    @Column(name = "device_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date deviceTime;

    @Column(name = "record_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.NONE)
    private Date recordTime;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "event_value", nullable = false, length = 8000)
    private String eventValue;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @PrePersist
    protected void onBeforeInsert() {
        recordTime = new Date();
    }

}
