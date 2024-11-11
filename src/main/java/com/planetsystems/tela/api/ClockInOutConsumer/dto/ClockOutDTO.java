package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClockOutDTO implements Serializable {

	private String id;

	@NotEmpty(message = "clockInId Id is required")
	@NotBlank(message = "clockInId Id is required")
	private String clockInId;

	@NotEmpty(message = "staffId Id is required")
	@NotBlank(message = "staffId Id is required")
	private String staffId;


	private String latitude;

	private String longitude;

	@NotEmpty(message = "clockOutDateTime Id is required")
	@NotBlank(message = "clockOutDateTime Id is required")
	private String clockOutDateTime;

	@NotEmpty(message = "telaSchoolNumber Id is required")
	@NotBlank(message = "telaSchoolNumber Id is required")
	private String telaSchoolNumber;

	@NotEmpty(message = "academicTermId Id is required")
	@NotBlank(message = "academicTermId Id is required")
	private String academicTermId;

	@NotEmpty(message = "clockOutType Id is required")
	@NotBlank(message = "clockOutType Id is required")
	private String clockOutType;

	private String comment;
	private String reason;
	private double displacement;

}
