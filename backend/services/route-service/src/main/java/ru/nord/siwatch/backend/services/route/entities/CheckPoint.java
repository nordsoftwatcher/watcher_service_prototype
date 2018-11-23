package ru.nord.siwatch.backend.services.route.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.services.common.entities.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "check_point")
@Getter
@Setter
public class CheckPoint extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @Column(name = "radius", nullable = false)
    private Double radius;

    @Column(name = "point_order", nullable = false)
    private Long order;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "name")
    private String name;

    @Column(name = "arrival_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalTime;

    @Column(name = "departure_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureTime;

    @Column(name = "plan_time")
    private Integer planTime;

    @Column(name = "fact_time")
    private Integer factTime;

    @Column(name = "address", length = 2000)
    private String address;

    @Column(name = "description", length = 10000)
    private String description;

}
