package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

@NamedEntityGraph(name = "time-attendance-supervision-detail-graph" , attributeNodes = {
		@NamedAttributeNode(value = "school"),
		@NamedAttributeNode(value = "schoolStaff", subgraph = "detail-sub-graph"),
		@NamedAttributeNode(value = "supervisor")
} ,
subgraphs = {
		@NamedSubgraph(name = "detail-sub-graph" , attributeNodes = {
				@NamedAttributeNode(value = "generalUserDetail")
		})
}
)
@Entity
@Table(name="TimeAttendanceSupervisions")
public class TimeAttendanceSupervision extends ParentEntity{

   private LocalDate supervisionDate;

   private LocalTime supervisionTime;
   private String comment;

   private Status attendanceStatus;

   @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
   @JoinColumn(name = "school_id")
   private School school;

   @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
   @JoinColumn(name = "schoolStaff_id")
   private SchoolStaff schoolStaff;

   @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
   @JoinColumn(name = "supervisor_id")
   private SystemUser supervisor;

   public TimeAttendanceSupervision() {
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

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public School getSchool() {
      return school;
   }

   public void setSchool(School school) {
      this.school = school;
   }

   public SchoolStaff getSchoolStaff() {
      return schoolStaff;
   }

   public void setSchoolStaff(SchoolStaff schoolStaff) {
      this.schoolStaff = schoolStaff;
   }

   public Status getAttendanceStatus() {
      return attendanceStatus;
   }

   public void setAttendanceStatus(Status attendanceStatus) {
      this.attendanceStatus = attendanceStatus;
   }

   public SystemUser getSupervisor() {
      return supervisor;
   }

   public void setSupervisor(SystemUser supervisor) {
      this.supervisor = supervisor;
   }
}
