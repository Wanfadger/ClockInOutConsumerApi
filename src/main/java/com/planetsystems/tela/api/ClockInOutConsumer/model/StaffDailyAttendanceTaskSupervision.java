package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SupervisionStatus;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

/**
 * recurring staff daily supervisions
 */
@Entity
@Table(name="StaffDailyAttendanceTaskSupervisions")
public class StaffDailyAttendanceTaskSupervision extends ParentEntity{

    /**
     * supervised attendance lessons under staffDailyAttendanceSupervision
     */
    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
    private StaffDailyTimeTableLesson staffDailyTimeTableLesson;


    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
    @JoinColumn(name = "staffDailyAttendanceSupervision_id")
    private StaffDailyAttendanceSupervision staffDailyAttendanceSupervision;



    private SupervisionStatus teachingTimeStatus;
    
    private SupervisionStatus teachingStatus;
    
    private String comment;

    public StaffDailyAttendanceTaskSupervision(String id) {
        super(id);
    }

    public StaffDailyAttendanceTaskSupervision() {
    }

    public StaffDailyAttendanceSupervision getStaffDailyAttendanceSupervision() {
        return staffDailyAttendanceSupervision;
    }

    public void setStaffDailyAttendanceSupervision(StaffDailyAttendanceSupervision staffDailyAttendanceSupervision) {
        this.staffDailyAttendanceSupervision = staffDailyAttendanceSupervision;
    }
 

    public SupervisionStatus getTeachingTimeStatus() {
        return teachingTimeStatus;
    }

    public void setTeachingTimeStatus(SupervisionStatus teachingTimeStatus) {
        this.teachingTimeStatus = teachingTimeStatus;
    }

	public SupervisionStatus getTeachingStatus() {
		return teachingStatus;
	}

	public void setTeachingStatus(SupervisionStatus teachingStatus) {
		this.teachingStatus = teachingStatus;
	}

	public StaffDailyTimeTableLesson getStaffDailyTimeTableLesson() {
		return staffDailyTimeTableLesson;
	}

	public void setStaffDailyTimeTableLesson(StaffDailyTimeTableLesson staffDailyTimeTableLesson) {
		this.staffDailyTimeTableLesson = staffDailyTimeTableLesson;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
