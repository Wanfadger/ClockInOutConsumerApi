package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name="Districts",indexes = {@Index(columnList = "code,name")})
//@Cache(region = "districtCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedEntityGraph(name = "district-region-school-graph" , attributeNodes = {
        @NamedAttributeNode("region"),
		@NamedAttributeNode("schools")})

@NamedEntityGraph(name = "districts-region-graph" , attributeNodes = {
        @NamedAttributeNode(value = "region")})


@Entity(name = "Districts")
@Setter
@Getter
@NoArgsConstructor
public class District extends ParentEntity implements Serializable {

	private String code;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @OneToMany(cascade=CascadeType.ALL , mappedBy = "district" , fetch = FetchType.LAZY)
    private Set<School> schools = new HashSet<>();

    private boolean rolledOut;

    public District(String id) {
        super(id);
    }


}
