package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum StaffType {

	TEACHER("Teacher"),//0
	HEADTEACHER("Head teacher"),//1
	DEPUTY_HEADTEACHER("Deputy head teacher"),//2
    SMC("Smc"),//3
    PRINCIPAL("Principal"),//4
    BURSAR("Bursar"),//5
    LIBRARIAN("Librarian"),//6
    LAB_TECH("Lab technician"),//7
    OTHERS("Others");//8

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
