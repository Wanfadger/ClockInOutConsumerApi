package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

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

	Status(String status) {
		this.status = status;
	}

	
	public String getStatus() {
		return status;
	}

	
	public void setStatus(String status) {
		this.status = status;
	}

	public static Status getStatus(String status) {
		for(Status status2:Status.values()) {
			if(status2.getStatus().equalsIgnoreCase(status)) {
				return status2;
			}
		}
		return null;
	}

	public static String getStatusValue(String status) {
		int i=0;
		for(Status status2:Status.values()) {
			if(status2.getStatus().equalsIgnoreCase(status)) {
				return Integer.toString(i);
			}
			i++;
		} 
		return null;
	}



}
