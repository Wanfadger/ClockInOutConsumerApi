package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

@NamedEntityGraph(name = "tela-schoolLicense-detail-graph" , attributeNodes = {
		@NamedAttributeNode(value = "telaLicenseKey"),
		@NamedAttributeNode(value = "school")
} 
)
@Entity
@Table(name = "TelaSchoolLicenses")
@Getter
@Setter
public class TelaSchoolLicense extends ParentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = {CascadeType.REFRESH , CascadeType.PERSIST}, fetch = FetchType.LAZY )
	private TelaLicenseKey telaLicenseKey;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private School school;

	private Status activationStatus;

	public TelaSchoolLicense() {

	}

	public TelaLicenseKey getTelaLicenseKey() {
		return telaLicenseKey;
	}

	public void setTelaLicenseKey(TelaLicenseKey telaLicenseKey) {
		this.telaLicenseKey = telaLicenseKey;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Status getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(Status activationStatus) {
		this.activationStatus = activationStatus;
	}

}
