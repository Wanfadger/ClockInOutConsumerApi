package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;


//@Entity
//@Table(name = "DataUploadTrails")
//@Cache(region = "dataUploadTrailCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "DataUploadTrails")
public class DataUploadTrail extends ParentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String region;
	private String district;
	private String school;
	private String level;
	private String schoolId;
	private String rolloutPhase;
	private long staffUploaded;
	private String comment;
	private String uploadedBy;

	public DataUploadTrail() {

	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getRolloutPhase() {
		return rolloutPhase;
	}

	public void setRolloutPhase(String rolloutPhase) {
		this.rolloutPhase = rolloutPhase;
	}

	public long getStaffUploaded() {
		return staffUploaded;
	}

	public void setStaffUploaded(long staffUploaded) {
		this.staffUploaded = staffUploaded;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

}
