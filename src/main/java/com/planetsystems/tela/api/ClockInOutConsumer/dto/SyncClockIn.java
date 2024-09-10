package com.planetsystems.tela.api.ClockInOutConsumer.dto;

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
public class SyncClockIn implements Serializable {
	private String id;
	private String type;
	private String employeeNo;
	private String employeeId;
	private String latitude;
	private String longitude;
	private String clockInDate;
	private String synStatus;
	private String day;
	private String clockInTime;
	private String telaSchoolNumber;

	@NotEmpty(message = "school Id is required")
	private String schoolId;

	@NotEmpty(message = "academicTerm Id is required")
	private String academicTermId;

	@NotEmpty(message = "school Id is required")
	private String siteNo;

	@NotEmpty(message = "school Id is required")
	private String schoolName;

	private String empFirstName;
	private String empLastName;

	private String dateCreated;
	private String dateUpdated;
	private String status;
	private String genId;
	
	private int localId;
	private boolean deleted;
	private boolean updated; 
	private boolean created;
	private String clockInType;
	
	//added
	private int displacement;

}
