package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Status {
	IN_COMPLETE("Incomplete"),
	COMPLETE("Complete"), 
	APPROVED("Approved"), 
	NEW("New"), 
	PENDING("Pending"),
	CANCELED("Canceled"), 
	ACTIVE("Active"), 
	IN_ACTIVE("In Active"),
	DELETED("Deleted"),
	ARCHIVED("Archived"), 
	NA("NA");  

	private String status;

	public static Optional<Status> fromString(String statusStr) {
		return Arrays.stream(Status.values()).parallel().filter(s -> s.getStatus().equalsIgnoreCase(statusStr)).findFirst();
	}

}
