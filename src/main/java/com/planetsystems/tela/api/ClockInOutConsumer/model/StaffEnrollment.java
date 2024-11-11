package com.planetsystems.tela.api.ClockInOutConsumer.model;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

/**
   persists staff head count details
   to be renamed later
 */

@NamedEntityGraph(name = "staffEnrollment-graph" , attributeNodes = {
  @NamedAttributeNode(value = "school"),
  @NamedAttributeNode(value = "academicTerm" ) } )
@Entity
@Table(name="StaffEnrollments")
public class StaffEnrollment extends ParentEntity {

	//This the teacher/Staff Census. it is conducted termly.
	//This could be re-factored into teacher census in the next version or release.
	
	@OneToOne(fetch = FetchType.LAZY)
	private School school;

	@OneToOne(fetch = FetchType.LAZY)
	private AcademicTerm academicTerm;

	private long totalMale;

	private long totalFemale;

	private Status enrollmentStatus;

	private LocalDate submissionDate;

	public StaffEnrollment() {
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public AcademicTerm getAcademicTerm() {
		return academicTerm;
	}

	public void setAcademicTerm(AcademicTerm academicTerm) {
		this.academicTerm = academicTerm;
	}

	public long getTotalMale() {
		return totalMale;
	}

	public void setTotalMale(long totalMale) {
		this.totalMale = totalMale;
	}

	public long getTotalFemale() {
		return totalFemale;
	}

	public void setTotalFemale(long totalFemale) {
		this.totalFemale = totalFemale;
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

	@Override
	public String toString() {
		return "StaffEnrollment{" +
				"totalMale=" + totalMale +
				", totalFemale=" + totalFemale +
				", enrollmentStatus=" + enrollmentStatus +
				", submissionDate=" + submissionDate +
				'}';
	}
}
