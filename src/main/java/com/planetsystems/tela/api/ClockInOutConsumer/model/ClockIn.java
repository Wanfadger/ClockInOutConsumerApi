package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.ClockedStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity(name = "ClockIns")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString
public class ClockIn extends ParentEntity {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolStaff_id")
	private SchoolStaff schoolStaff;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;

	@OneToOne(mappedBy = "clockIn", fetch = FetchType.LAZY , targetEntity = ClockOut.class)
	private ClockOut clockOut;

	private LocalDate clockInDate;

	private LocalTime clockInTime;

	private String comment;
	private String latitude;
	private String longitude;
	private ClockedStatus clockedStatus;


	private String clockinType;
	private Integer displacement;
	public ClockIn(String id) {
		super(id);
	}


}
