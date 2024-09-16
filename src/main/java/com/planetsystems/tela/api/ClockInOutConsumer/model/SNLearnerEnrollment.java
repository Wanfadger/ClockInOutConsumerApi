package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

//Learners with special needs head count
@Entity
@Table(name = "SNLearnerEnrollments")
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

	@Transient()
	private int localId;

	public SNLearnerEnrollment() {

	}

	public SchoolClass getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public long getTotalBoys() {
		return totalBoys;
	}

	public void setTotalBoys(long totalBoys) {
		this.totalBoys = totalBoys;
	}

	public long getTotalGirls() {
		return totalGirls;
	}

	public void setTotalGirls(long totalGirls) {
		this.totalGirls = totalGirls;
	}

	public Status getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(Status enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}

	public LocalDate getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(LocalDate submissionDate) {
		this.submissionDate = submissionDate;
	}

	public SchoolStaff getSchoolStaff() {
		return schoolStaff;
	}

	public void setSchoolStaff(SchoolStaff schoolStaff) {
		this.schoolStaff = schoolStaff;
	}

	public int getLocalId() {
		return localId;
	}

	public void setLocalId(int localId) {
		this.localId = localId;
	}
}
