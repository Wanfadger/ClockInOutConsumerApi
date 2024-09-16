package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.LessonDay;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

@NamedEntityGraph(name = "timeTable-lesson-detail-graph" , attributeNodes = {
		@NamedAttributeNode(value = "schoolClass", subgraph = "schoolClass-sub-graph"),
		@NamedAttributeNode(value = "schoolStaff"),
		@NamedAttributeNode(value = "subject"),
		@NamedAttributeNode(value = "timeTable")
} ,
subgraphs = {
		@NamedSubgraph(name = "schoolClass-sub-graph" , attributeNodes = {
				@NamedAttributeNode(value = "school"),
				@NamedAttributeNode(value = "academicTerm")
		})
}
)
@Entity
@Table(name="TimeTableLessons")
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

    public TimeTableLesson() {
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public SchoolStaff getSchoolStaff() {
        return schoolStaff;
    }

    public void setSchoolStaff(SchoolStaff schoolStaff) {
        this.schoolStaff = schoolStaff;
    }

    public LessonDay getLessonDay() {
        return lessonDay;
    }

    public void setLessonDay(LessonDay lessonDay) {
        this.lessonDay = lessonDay;
    }

    public TimeTable getTimeTable() {
		return timeTable;
	}

	public void setTimeTable(TimeTable timeTable) {
		this.timeTable = timeTable;
	}



    public LocalTime getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(LocalTime breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public LocalTime getBreakEndTime() {
        return breakEndTime;
    }

    public void setBreakEndTime(LocalTime breakEndTime) {
        this.breakEndTime = breakEndTime;
    }

    public LocalTime getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(LocalTime lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public LocalTime getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(LocalTime lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public LocalTime getClassStartTime() {
        return classStartTime;
    }

    public void setClassStartTime(LocalTime classStartTime) {
        this.classStartTime = classStartTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalTime getClassEndTime() {
        return classEndTime;
    }

    public void setClassEndTime(LocalTime classEndTime) {
        this.classEndTime = classEndTime;
    }

    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return lessonDay.getDay();
	}
}
