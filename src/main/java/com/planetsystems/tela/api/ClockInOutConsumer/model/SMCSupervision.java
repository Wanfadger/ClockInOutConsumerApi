package com.planetsystems.tela.api.ClockInOutConsumer.model;


import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.*;
@NamedEntityGraph(name = "smc-supervision-detail-graph" , attributeNodes = {
		@NamedAttributeNode(value = "academicTerm"),
		@NamedAttributeNode(value = "school"),
		@NamedAttributeNode(value = "supervisor", subgraph = "supervisor-sub-graph")
} ,
subgraphs = {
		@NamedSubgraph(name = "supervisor-sub-graph" , attributeNodes = {
				@NamedAttributeNode(value = "generalUserDetail")
		})
}
)
@Entity
@Table(name="SMCSupervisions")
public class SMCSupervision extends ParentEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private AcademicTerm academicTerm;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private School school;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SchoolStaff supervisor;

	private long staffPresent;
	private long staffAtWork;
	private String comment;

	@Temporal(TemporalType.DATE)
	private Date submissionDate;

	private long staffAtWorkNotWorking;
	private long staffAtWorkWorking;
	private AttendanceStatus supervisorStatus;

	private AttendanceStatus status_p1;
	private AttendanceStatus status_p2;
	private AttendanceStatus status_p3;
	private AttendanceStatus status_p4;
	private AttendanceStatus status_p5;
	private AttendanceStatus status_p6;
	private AttendanceStatus status_p7;

	public SMCSupervision() {

	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public long getStaffPresent() {
		return staffPresent;
	}

	public void setStaffPresent(long staffPresent) {
		this.staffPresent = staffPresent;
	}

	public long getStaffAtWork() {
		return staffAtWork;
	}

	public void setStaffAtWork(long staffAtWork) {
		this.staffAtWork = staffAtWork;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public SchoolStaff getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(SchoolStaff supervisor) {
		this.supervisor = supervisor;
	}

	public long getStaffAtWorkNotWorking() {
		return staffAtWorkNotWorking;
	}

	public void setStaffAtWorkNotWorking(long staffAtWorkNotWorking) {
		this.staffAtWorkNotWorking = staffAtWorkNotWorking;
	}

	public long getStaffAtWorkWorking() {
		return staffAtWorkWorking;
	}

	public void setStaffAtWorkWorking(long staffAtWorkWorking) {
		this.staffAtWorkWorking = staffAtWorkWorking;
	}

	public AttendanceStatus getSupervisorStatus() {
		return supervisorStatus;
	}

	public void setSupervisorStatus(AttendanceStatus supervisorStatus) {
		this.supervisorStatus = supervisorStatus;
	}

	public AttendanceStatus getStatus_p1() {
		return status_p1;
	}

	public void setStatus_p1(AttendanceStatus status_p1) {
		this.status_p1 = status_p1;
	}

	public AttendanceStatus getStatus_p2() {
		return status_p2;
	}

	public void setStatus_p2(AttendanceStatus status_p2) {
		this.status_p2 = status_p2;
	}

	public AttendanceStatus getStatus_p3() {
		return status_p3;
	}

	public void setStatus_p3(AttendanceStatus status_p3) {
		this.status_p3 = status_p3;
	}

	public AttendanceStatus getStatus_p4() {
		return status_p4;
	}

	public void setStatus_p4(AttendanceStatus status_p4) {
		this.status_p4 = status_p4;
	}

	public AttendanceStatus getStatus_p5() {
		return status_p5;
	}

	public void setStatus_p5(AttendanceStatus status_p5) {
		this.status_p5 = status_p5;
	}

	public AttendanceStatus getStatus_p6() {
		return status_p6;
	}

	public void setStatus_p6(AttendanceStatus status_p6) {
		this.status_p6 = status_p6;
	}

	public AttendanceStatus getStatus_p7() {
		return status_p7;
	}

	public void setStatus_p7(AttendanceStatus status_p7) {
		this.status_p7 = status_p7;
	}

	public AcademicTerm getAcademicTerm() {
		return academicTerm;
	}

	public void setAcademicTerm(AcademicTerm academicTerm) {
		this.academicTerm = academicTerm;
	}

}
