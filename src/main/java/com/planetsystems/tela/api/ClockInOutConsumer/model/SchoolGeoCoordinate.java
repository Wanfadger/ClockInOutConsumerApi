package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.*;

@Entity(name = "SchoolGeoCoordinates")
@Setter
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(name = "school", columnNames = {"school_id"})})
public class SchoolGeoCoordinate extends ParentEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;
    private double longtitude;
    private double latitude;
    private boolean geoFenceActivated=true;
    private boolean pinClockActivated=false;
    private int displacement=250; //100M

}
