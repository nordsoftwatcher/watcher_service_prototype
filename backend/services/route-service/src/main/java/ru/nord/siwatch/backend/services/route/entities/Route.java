package ru.nord.siwatch.backend.services.route.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractEntity;
import ru.nord.siwatch.backend.services.route.enums.RouteStatus;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "route")
@Getter
@Setter
public class Route extends AbstractEntity {

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private List<RoutePoint> routePoints;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RouteStatus status;

}
