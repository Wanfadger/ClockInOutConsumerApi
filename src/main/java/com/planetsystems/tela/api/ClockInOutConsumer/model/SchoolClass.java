package com.planetsystems.tela.api.ClockInOutConsumer.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.*;

@Entity(name = "SchoolClasses")
public class SchoolClass extends ParentEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String code;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToMany(mappedBy = "schoolClass" , fetch = FetchType.LAZY)
	private Set<TimeTableLesson> timeTableLessons = new HashSet<>();

	private boolean hasStreams;
	private boolean classLevel;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SchoolClass parentSchoolClass;

//	@OneToOne(mappedBy = "schoolClass" , fetch = FetchType.LAZY)
//	private LearnerEnrollment learnerEnrollment;

//	@OneToOne(mappedBy = "schoolClass" , fetch = FetchType.LAZY)
//	private LearnerAttendance learnerAttendance;


	public SchoolClass() {
	}

	public SchoolClass(String id) {
		super(id);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String deploymentUnit) {
		this.name = deploymentUnit;
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

	public boolean isHasStreams() {
		return hasStreams;
	}

	public void setHasStreams(boolean hasStreams) {
		this.hasStreams = hasStreams;
	}

	public boolean isClassLevel() {
		return classLevel;
	}

	public void setClassLevel(boolean classLevel) {
		this.classLevel = classLevel;
	}

	public SchoolClass getParentSchoolClass() {
		return parentSchoolClass;
	}

	public void setParentSchoolClass(SchoolClass parentSchoolClass) {
		this.parentSchoolClass = parentSchoolClass;
	}

//	public LearnerEnrollment getLearnerEnrollment() {
//		return learnerEnrollment;
//	}
//
//	public void setLearnerEnrollment(LearnerEnrollment learnerEnrollment) {
//		this.learnerEnrollment = learnerEnrollment;
//	}

//	public LearnerAttendance getLearnerAttendance() {
//		return learnerAttendance;
//	}
//
//	public void setLearnerAttendance(LearnerAttendance learnerAttendance) {
//		this.learnerAttendance = learnerAttendance;
//	}

	public Set<TimeTableLesson> getTimeTableLessons() {
		return timeTableLessons;
	}

	public void setTimeTableLessons(Set<TimeTableLesson> timeTableLessons) {
		this.timeTableLessons = timeTableLessons;
	}
}
