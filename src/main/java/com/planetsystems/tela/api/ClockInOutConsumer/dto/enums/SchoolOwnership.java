package com.planetsystems.tela.api.ClockInOutConsumer.dto.enums;

public enum SchoolOwnership {

	GOVT("Government"), PRIVATE("Private");

	private String ownership;

	SchoolOwnership(String ownership) {
		this.ownership = ownership;
	}

	public String getSchoolOwnership() {
		return ownership;
	}

	public void setSchoolOwnership(String ownership) {
		this.ownership = ownership;
	}

	public static SchoolOwnership getSchoolOwnership(String ownership) {
		for (SchoolOwnership schoolOwnership : SchoolOwnership.values()) {
			if (schoolOwnership.getSchoolOwnership().equalsIgnoreCase(ownership)) {
				return schoolOwnership;
			}
		}
		return null;
	}

}

