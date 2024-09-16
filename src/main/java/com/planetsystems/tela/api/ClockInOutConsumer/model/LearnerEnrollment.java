package com.planetsystems.tela.api.ClockInOutConsumer.model;


import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="LearnerEnrollments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LearnerEnrollment extends ParentEntity{

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolClass_id")
    private SchoolClass schoolClass;
    private long totalBoys;
    private long totalGirls; 
    //Submitted by staff
    private Status enrollmentStatus;
	private LocalDate submissionDate;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolStaff_id")
	private SchoolStaff schoolStaff;


}
