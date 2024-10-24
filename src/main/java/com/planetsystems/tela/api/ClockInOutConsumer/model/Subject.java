package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SubjectClassification;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;


@Entity
@Table(name = "Subjects")
public class Subject extends ParentEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private SubjectCategory subjectCategory;

	@OneToMany(mappedBy = "subject")
	private Set<TimeTableLesson> timeTableLessons = new HashSet<>();

	private SubjectClassification subjectClassification;

	public Subject() {
	}

	public Subject(String id) {
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

	public void setName(String name) {
		this.name = name;
	}

	public SubjectCategory getSubjectCategory() {
		return subjectCategory;
	}

	public void setSubjectCategory(SubjectCategory subjectCategory) {
		this.subjectCategory = subjectCategory;
	}

	public SubjectClassification getSubjectClassification() {
		return subjectClassification;
	}

	public void setSubjectClassification(SubjectClassification subjectClassification) {
		this.subjectClassification = subjectClassification;
	}

	public Set<TimeTableLesson> getTimeTableLessons() {
		return timeTableLessons;
	}

	public void setTimeTableLessons(Set<TimeTableLesson> timeTableLessons) {
		this.timeTableLessons = timeTableLessons;
	}
}
