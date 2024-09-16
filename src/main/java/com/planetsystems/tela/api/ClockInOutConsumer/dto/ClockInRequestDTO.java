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
public class ClockInRequestDTO implements Serializable {
	@NotEmpty(message = "telaSchoolNumber Id is required")
	@NotBlank(message = "telaSchoolNumber Id is required")
	private String staffId;

	@NotEmpty(message = "telaSchoolNumber Id is required")
	@NotBlank(message = "telaSchoolNumber Id is required")
	private String latitude;

	@NotEmpty(message = "telaSchoolNumber Id is required")
	@NotBlank(message = "telaSchoolNumber Id is required")
	private String longitude;

	@NotEmpty(message = "clockInDateTime Id is required")
	@NotBlank(message = "clockInDateTime Id is required")
	private String clockInDateTime;

	@NotEmpty(message = "telaSchoolNumber Id is required")
	@NotBlank(message = "telaSchoolNumber Id is required")
	private String telaSchoolNumber;

	@NotEmpty(message = "academicTermId Id is required")
	@NotBlank(message = "academicTermId Id is required")
	private String academicTermId;

	@NotEmpty(message = "clockInType Id is required")
	@NotBlank(message = "clockInType Id is required")
	private String clockInType;

	private int displacement;

}
