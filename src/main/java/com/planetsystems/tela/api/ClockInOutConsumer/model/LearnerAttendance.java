package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;


//@Entity
//@Table(name="LearnerAttendances",indexes = {@Index(columnList = "id,attendanceDate,girlsPresent,boysPresent,boysAbsent,comment")})
//@Cache(region = "learnerAttendanceCache", usage = CacheConcurrencyStrategy.READ_WRITE)

@NamedEntityGraph(name = "learnerAttendance-with-staffDetails-graph", attributeNodes = {
		@NamedAttributeNode(value = "schoolStaff", subgraph = "staffDetails-sub-graph"),
		@NamedAttributeNode(value = "academicTerm"), @NamedAttributeNode(value = "schoolClass"), }, subgraphs = {
				@NamedSubgraph(name = "staffDetails-sub-graph", attributeNodes = {
						@NamedAttributeNode("generalUserDetail"), }) })
@Entity(name = "LearnerAttendances")
public class LearnerAttendance extends ParentEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolClass_id")
	private SchoolClass schoolClass;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolStaff_id")
	private SchoolStaff schoolStaff;

	private LocalDate attendanceDate;

	private long girlsPresent;
	private long boysPresent;
	private long boysAbsent;
	private long girlsAbsent;
	private String comment;

	public LearnerAttendance() {
	}

    @Transient()
    private int localId;
    
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