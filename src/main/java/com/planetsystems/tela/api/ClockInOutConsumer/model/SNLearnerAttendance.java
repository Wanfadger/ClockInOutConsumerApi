package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

@Entity
//@Table(name = "SNLearnerAttendances", indexes = {
//		@Index(columnList = "id,attendanceDate,girlsPresent,boysPresent,boysAbsent,comment") })
//@Cache(region = "snLearnerAttendanceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "SNLearnerAttendances")
@NamedEntityGraph(name = "snlearnerattendance-schoolclass-academicterm-staff-graph", attributeNodes = {
		@NamedAttributeNode(value = "schoolClass"),
		@NamedAttributeNode(value = "academicTerm"),
		@NamedAttributeNode(value = "schoolStaff") })
public class SNLearnerAttendance extends ParentEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5660869902917768992L;

	@OneToOne(fetch = FetchType.LAZY)
	private SchoolClass schoolClass;

	@OneToOne(fetch = FetchType.LAZY)
	private AcademicTerm academicTerm;

	@OneToOne(fetch = FetchType.LAZY)
	private SchoolStaff schoolStaff;

	private LocalDate attendanceDate;

	private long girlsPresent;
	private long boysPresent;
	private long boysAbsent;
	private long girlsAbsent;
	private String comment;

	@Transient()
	private int localId;

	public SNLearnerAttendance() {

	}

	public SchoolClass getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public AcademicTerm getAcademicTerm() {
		return academicTerm;
	}

	public void setAcademicTerm(AcademicTerm academicTerm) {
		this.academicTerm = academicTerm;
	}

	public SchoolStaff getSchoolStaff() {
		return schoolStaff;
	}

	public void setSchoolStaff(SchoolStaff schoolStaff) {
		this.schoolStaff = schoolStaff;
	}

	public LocalDate getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(LocalDate attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public long getGirlsPresent() {
		return girlsPresent;
	}

	public void setGirlsPresent(long girlsPresent) {
		this.girlsPresent = girlsPresent;
	}

	public long getBoysPresent() {
		return boysPresent;
	}

	public void setBoysPresent(long boysPresent) {
		this.boysPresent = boysPresent;
	}

	public long getBoysAbsent() {
		return boysAbsent;
	}

	public void setBoysAbsent(long boysAbsent) {
		this.boysAbsent = boysAbsent;
	}

	public long getGirlsAbsent() {
		return girlsAbsent;
	}

	public void setGirlsAbsent(long girlsAbsent) {
		this.girlsAbsent = girlsAbsent;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getLocalId() {
		return localId;
	}

	public void setLocalId(int localId) {
		this.localId = localId;
	}
}
