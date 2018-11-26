package ru.nord.siwatch.backend.services.route.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractEntity;
import ru.nord.siwatch.backend.services.route.enums.RouteStatus;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "route")
@Getter
@Setter
public class Route extends AbstractEntity {

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private Set<RoutePoint> routePoints;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private Set<CheckPoint> checkPoints;

    @Column(name = "name")
    private String name;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "supervisor_id", nullable = false)
    private Long supervisorId;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RouteStatus status;

}
