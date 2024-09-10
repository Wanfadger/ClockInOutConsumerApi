package com.planetsystems.tela.api.ClockInOutConsumer.dto.enums;

public enum StaffType {

	TEACHER("Teacher"),
	HEADTEACHER("Head teacher"),
	DEPUTY_HEADTEACHER("Deputy head teacher"),
    SMC("Smc"),
    PRINCIPAL("Principal"),
    BURSAR("Bursar"),
    LIBRARIAN("Librarian"),
    LAB_TECH("Lab technician"),
    OTHERS("Others"),
  //added for staff transfer authorization
  	SUPPORT("Support Staff");//9

	private String type;

	StaffType(String type) {
		this.type = type;
	}

	public String getStaffType() {
		return type;
	}

	public void setStaffType(String type) {
		this.type = type;
	}

	public static StaffType getStaffType(String type) {
		for (StaffType staffType : StaffType.values()) {
			if (staffType.getStaffType().equalsIgnoreCase(type)) {
				return staffType;
			}
		}
		return null;
	}

}

