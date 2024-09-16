package com.planetsystems.tela.api.ClockInOutConsumer.model;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

@NamedEntityGraph(name = "timeTable-detail-graph" , attributeNodes = {
		@NamedAttributeNode(value = "school"),
		@NamedAttributeNode(value = "academicTerm"),
		//@NamedAttributeNode(value = "timeTableLessons")
}
)
@NamedEntityGraph(name = "timeTable-graph" , attributeNodes = {
		@NamedAttributeNode(value = "school"),
		//@NamedAttributeNode(value = "academicTerm"),
		//@NamedAttributeNode(value = "timeTableLessons")
}
)
@Entity
@Table(name="TimeTables")
public class TimeTable extends ParentEntity {

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;


	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = TimeTableLesson.class, mappedBy = "timeTable")
	private List<TimeTableLesson> timeTableLessons;

	public TimeTable() {
	}

	public TimeTable(String id) {
		super(id);
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

	public List<TimeTableLesson> getTimeTableLessons() {
		return timeTableLessons;
	}

	public void setTimeTableLessons(List<TimeTableLesson> timeTableLessons) {
		this.timeTableLessons = timeTableLessons;
	}


}
