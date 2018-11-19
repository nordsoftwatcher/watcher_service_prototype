package ru.nord.siwatch.backend.services.route.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "route_point")
@Getter
@Setter
public class RoutePoint extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @Column(name = "point_order", nullable = false)
    private Long order;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "altitude", nullable = false)
    private Double altitude;

    @Embedded
    private RoutePointInfo pointInfo;

}
