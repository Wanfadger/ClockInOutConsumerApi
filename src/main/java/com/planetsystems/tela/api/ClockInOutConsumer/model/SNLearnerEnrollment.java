package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.*;
import java.util.*;

//Learners with special needs head count
@Entity
@Table(name = "SNLearnerEnrollments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SNLearnerEnrollment extends ParentEntity{

	private static final long serialVersionUID = 1L;
	
	@OneToOne(fetch = FetchType.LAZY)
	private SchoolClass schoolClass;
	private long totalBoys;
	private long totalGirls;

	private Status enrollmentStatus;

	private LocalDate submissionDate;

	// Submitted by staff
	@OneToOne(fetch = FetchType.LAZY)
	private SchoolStaff schoolStaff;


}
