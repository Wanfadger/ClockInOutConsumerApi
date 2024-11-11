package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.LessonDay;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

//@NamedEntityGraph(name = "timeTable-lesson-detail-graph" , attributeNodes = {
//		@NamedAttributeNode(value = "schoolClass", subgraph = "schoolClass-sub-graph"),
//		@NamedAttributeNode(value = "schoolStaff"),
//		@NamedAttributeNode(value = "subject"),
//		@NamedAttributeNode(value = "timeTable")
//} ,
//subgraphs = {
//		@NamedSubgraph(name = "schoolClass-sub-graph" , attributeNodes = {
//				@NamedAttributeNode(value = "school"),
//				@NamedAttributeNode(value = "academicTerm")
//		})
//}
//)
@Entity
@Table(name="TimeTableLessons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableLesson extends ParentEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LessonDay lessonDay;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolClass_id")
    private SchoolClass schoolClass;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Subject subject;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer duration;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;

    private LocalTime classStartTime;
    private LocalTime classEndTime;


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private SchoolStaff schoolStaff;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "timeTable_id")
    private TimeTable  timeTable;

}
