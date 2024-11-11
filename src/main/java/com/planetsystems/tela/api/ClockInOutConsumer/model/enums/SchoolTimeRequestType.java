package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum SchoolTimeRequestType {
	TIME_OFF("Time-off"),MEETING("Meeting");

	private String type;

	SchoolTimeRequestType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static SchoolTimeRequestType getRequestType(String type) {
		for (SchoolTimeRequestType requestType : SchoolTimeRequestType.values()) {
			if (requestType.getType().equalsIgnoreCase(type)) {
				return requestType;
			}
		}
		return null;
	}


}
