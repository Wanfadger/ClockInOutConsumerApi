package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

/**
 root staff daily task supervision
 supervisions persisted into StaffDailyAttendanceTaskSupervisions
 */

/**
 * to be renamed to StaffDailyTimeTableSupervisions to improve readability and meaning
 * recurring to StaffDailyTimeTableLessonSupervisions
 */
@NamedEntityGraph(name = "staff-daily-attendance-supervision-graph" , attributeNodes = {
		@NamedAttributeNode("supervisor"),
		@NamedAttributeNode("schoolStaff")}
)
@Entity
@Table(name="StaffDailyAttendanceSupervisions")
public class StaffDailyAttendanceSupervision extends ParentEntity{

	//to removed
    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private SchoolStaff supervisor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolStaff_id")
    private SchoolStaff schoolStaff;

    private String comment;

    private LocalDate supervisionDate;

    private LocalTime supervisionTime;
     
    private AttendanceStatus attendanceStatus;

    /**
     * Supervised tasks
     */
    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY , targetEntity = StaffDailyAttendanceTaskSupervision.class , mappedBy = "staffDailyAttendanceSupervision")
    private Set<StaffDailyAttendanceTaskSupervision> staffDailyAttendanceTaskSupervisions = new HashSet<>();

    public StaffDailyAttendanceSupervision() {
    }

    public StaffDailyAttendanceSupervision(String id) {
        super(id);
    }

    public SchoolStaff getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SchoolStaff supervisor) {
        this.supervisor = supervisor;
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

    public LocalDate getSupervisionDate() {
        return supervisionDate;
    }

    public void setSupervisionDate(LocalDate supervisionDate) {
        this.supervisionDate = supervisionDate;
    }

    public LocalTime getSupervisionTime() {
        return supervisionTime;
    }

    public void setSupervisionTime(LocalTime supervisionTime) {
        this.supervisionTime = supervisionTime;
    }

    public Set<StaffDailyAttendanceTaskSupervision> getStaffDailyAttendanceTaskSupervisions() {
        return staffDailyAttendanceTaskSupervisions;
    }

    public void setStaffDailyAttendanceTaskSupervisions(Set<StaffDailyAttendanceTaskSupervision> staffDailyAttendanceTaskSupervisions) {
        this.staffDailyAttendanceTaskSupervisions = staffDailyAttendanceTaskSupervisions;
    }

    public AttendanceStatus getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

}
