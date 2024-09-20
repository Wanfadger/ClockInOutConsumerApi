package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.ClockedStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

import java.util.Set;
@Entity(name = "ClockOuts")
//@Table(name="ClockOuts",indexes = {@Index(columnList = "id,clockOutTime,clockedStatus")})
//@Cache(region = "clockOutCache", usage = CacheConcurrencyStrategy.READ_WRITE)

//@NamedEntityGraph(name = "clockOut-clockIn-graph", attributeNodes = {
//		@NamedAttributeNode(value = "clockIn", subgraph = "school-sub-graph") }, subgraphs = {
//				@NamedSubgraph(name = "school-sub-graph", attributeNodes = @NamedAttributeNode(value = "school")) })

@Setter
@Getter
@NoArgsConstructor
public class ClockOut extends ParentEntity {

	@OneToOne(fetch = FetchType.LAZY , targetEntity = ClockIn.class)
	@JoinColumn(name = "clockIn_id", unique = true)
	private ClockIn clockIn;

	private LocalDate clockOutDate;

	private LocalTime clockOutTime;

	private ClockedStatus clockedStatus;
    
    private String comment;
    // add enum to track clockinout type [PIN , FACE , FINGER , AUTO]

	private String clockOutType;

	private Integer displacement;


}
