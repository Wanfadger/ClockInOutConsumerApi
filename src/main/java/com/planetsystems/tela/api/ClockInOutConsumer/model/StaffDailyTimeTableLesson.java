package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

/**
 * Staff daily attendance lessons
 */
@NamedEntityGraph(name = "staff-daily-timeTable-lesson-detail-graph" , attributeNodes = {
		@NamedAttributeNode(value = "schoolClass"),
		@NamedAttributeNode(value = "subject"),
		@NamedAttributeNode(value = "staffDailyTimeTable")
}
)
@Entity
@Table(name="StaffDailyTimeTableLessons")
public class StaffDailyTimeTableLesson extends ParentEntity{

    /**
	 * 
	 */ 
	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
    private SchoolClass schoolClass;

    @OneToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @Temporal(TemporalType.DATE)
    private Date lessonDate;

    private LocalTime startTime;
    private LocalTime endTime;
    private AttendanceStatus dailyTimeTableLessonStatus;

    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
    private StaffDailyTimeTable staffDailyTimeTable;

    public StaffDailyTimeTableLesson() {
    }

    public StaffDailyTimeTableLesson(String id) {
        super(id);
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

    public AttendanceStatus getDailyTimeTableLessonStatus() {
        return dailyTimeTableLessonStatus;
    }

    public void setDailyTimeTableLessonStatus(AttendanceStatus dailyTimeTableLessonStatus) {
        this.dailyTimeTableLessonStatus = dailyTimeTableLessonStatus;
    }

    public StaffDailyTimeTable getStaffDailyTimeTable() {
        return staffDailyTimeTable;
    }

    public void setStaffDailyTimeTable(StaffDailyTimeTable staffDailyTimeTable) {
        this.staffDailyTimeTable = staffDailyTimeTable;
    }

    public Date getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(Date lessonDate) {
        this.lessonDate = lessonDate;
    }

}
