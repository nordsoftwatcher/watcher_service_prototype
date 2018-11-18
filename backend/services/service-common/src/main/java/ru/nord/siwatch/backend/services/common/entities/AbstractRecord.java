package ru.nord.siwatch.backend.services.common.entities;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractRecord extends AbstractEntity
{
    @Column(name = "device_id", nullable = false, length = 32)
    @NotBlank @Size(max = 32)
    private String deviceId;

    @Column(name = "device_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date deviceTime;

    @Column(name = "record_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.NONE)
    private Date recordTime;

    @PrePersist
    protected void onBeforeInsert() {
        recordTime = new Date();
    }
}
