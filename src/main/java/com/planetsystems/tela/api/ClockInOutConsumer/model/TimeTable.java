package com.planetsystems.tela.api.ClockInOutConsumer.model;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

//@NamedEntityGraph(name = "timeTable-detail-graph" , attributeNodes = {
//		@NamedAttributeNode(value = "school"),
//		@NamedAttributeNode(value = "academicTerm"),
//		//@NamedAttributeNode(value = "timeTableLessons")
//}
//)
//@NamedEntityGraph(name = "timeTable-graph" , attributeNodes = {
//		@NamedAttributeNode(value = "school"),
//		//@NamedAttributeNode(value = "academicTerm"),
//		//@NamedAttributeNode(value = "timeTableLessons")
//}
//)
@Entity
@Table(name="TimeTables")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable extends ParentEntity {

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;


	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = TimeTableLesson.class, mappedBy = "timeTable")
	private List<TimeTableLesson> timeTableLessons;

	public TimeTable(String id) {
		super(id);
	}



}
