package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum AdministrationLevel {

	SCHOOL("School"), 
	DISTRICT("District"), 
	MUNICIPLE("Municiple"),
	REGION("Region"),
	NATIONAL("National");

	private String level;

	AdministrationLevel(String level) {
		this.level = level;
	}

	public String getAdministrationLevel() {
		return level;
	}

	public void setAdministrationLevel(String level) {
		this.level = level;
	}

	public static AdministrationLevel getAdministrationLevel(String level) {
		for (AdministrationLevel sl : AdministrationLevel.values()) {
			if (sl.getAdministrationLevel().equalsIgnoreCase(level)) {
				return sl;
			}
		}
		return null;
	}

}
