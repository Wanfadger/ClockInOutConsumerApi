package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.*;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(name = "region-district-graph" , attributeNodes = {
		@NamedAttributeNode("districts")
		}
)


@Entity
@Table(name="Regions")
@Setter
@Getter
@NoArgsConstructor
public class Region extends ParentEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
    private String name;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "region" , fetch = FetchType.LAZY)
    @Where(clause = "status != 8")
    private Set<District> districts = new HashSet<>();

	public Region(String id) {
		super(id);
	}
}
