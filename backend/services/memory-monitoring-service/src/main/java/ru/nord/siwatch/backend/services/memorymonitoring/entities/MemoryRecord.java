package ru.nord.siwatch.backend.services.memorymonitoring.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "memory_record_log")
@Getter
@Setter
public class MemoryRecord extends AbstractRecord {

    /**
     * Free system memory (bytes)
     */
    @Column(name = "free_system_memory", nullable = false)
    private Long freeSystemMemory;

    /**
     * Free storage memory (bytes)
     */
    @Column(name = "free_storage_memory", nullable = false)
    private Long freeStorageMemory;

}
