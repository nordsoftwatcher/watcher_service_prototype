package ru.nord.siwatch.backend.services.common.entities;

import javax.persistence.*;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEntity
{
    @Access(AccessType.PROPERTY) @Column(name = "id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public Long getId() { return this.id; }
    protected void setId(Long id) { this.id = id; }
}
