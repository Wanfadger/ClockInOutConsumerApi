package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="SchoolMaterials")
public class SchoolMaterial extends ParentEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String materialName;
	
	public SchoolMaterial() {
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
}
