package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelaPublicHoliday extends ParentEntity {

	@Column(nullable = false)
	private String name;
	private String description;

	@Column(nullable = false)
	private LocalDate date;

}
