package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

/**
 * to be renamed to StaffDailyTimeTable to improve readability and meaning
 * recurring to StaffDailyTimeTableLessons
 */
@NamedEntityGraph(name = "staff-daily-timetable-graph", attributeNodes = {
		@NamedAttributeNode(value = "academicTerm"),
		@NamedAttributeNode(value = "schoolStaff"),
		//@NamedAttributeNode(value = "staffDailyTimeTableLessons")
})
@Entity
@Table(name="StaffDailyTimeTables")
public class StaffDailyTimeTable extends ParentEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
    private AcademicTerm academicTerm;

    @OneToOne(fetch = FetchType.LAZY)
    private SchoolStaff schoolStaff;

    private String comment;

    @Temporal(TemporalType.DATE)
    private Date lessonDate;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "staffDailyTimeTable" , targetEntity = StaffDailyTimeTableLesson.class)
    private List<StaffDailyTimeTableLesson> staffDailyTimeTableLessons;

    public StaffDailyTimeTable() {
    }

    public StaffDailyTimeTable(String id) {
        super(id);
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<StaffDailyTimeTableLesson> getStaffDailyTimeTableLessons() {
        return staffDailyTimeTableLessons;
    }

    public void setStaffDailyTimeTableLessons(List<StaffDailyTimeTableLesson> staffDailyTimeTableLessons) {
        this.staffDailyTimeTableLessons = staffDailyTimeTableLessons;
    }

    public Date getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(Date lessonDate) {
        this.lessonDate = lessonDate;
    }
}
