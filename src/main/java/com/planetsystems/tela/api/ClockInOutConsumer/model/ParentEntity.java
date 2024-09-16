package com.planetsystems.tela.api.ClockInOutConsumer.model;


import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.*;

@MappedSuperclass
@Setter
@Getter
@SuperBuilder
public class ParentEntity implements Serializable {

	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid") 
	@Id
	private String id;

	@CreationTimestamp
	private LocalDateTime createdDateTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateTime;
	private Status status = Status.ACTIVE;

	public ParentEntity() {
	}

	public ParentEntity(String id) {
		this.id = id;
	}

	public ParentEntity(LocalDateTime createdDateTime, LocalDateTime updatedDateTime , Status status) {
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
		this.status = status;
	}

	public String getId() {
		return id;
	}

}
