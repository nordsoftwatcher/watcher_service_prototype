package ru.nord.siwatch.backend.services.route.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
@Getter
@Setter
public class RoutePointInfo {

    @Column(name = "name")
    private String name;

    @Column(name = "arrival_time")
    @Temporal(TemporalType.TIME)
    private Date arrivalTime;

    @Column(name = "departure_time")
    @Temporal(TemporalType.TIME)
    private Date departureTime;

    @Column(name = "plan_time")
    private Integer planTime;

    @Column(name = "fact_time")
    private Integer factTime;

    @Column(name = "address", length = 2000)
    private String address;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "passed")
    private Boolean passed;

}
