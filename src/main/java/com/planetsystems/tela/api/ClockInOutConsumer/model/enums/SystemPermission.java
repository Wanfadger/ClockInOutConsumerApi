package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum SystemPermission {
	All_Items("All Items"),
	System_Users("System Users"),
	Notifications("Notifications"),
	Geographics("Geographics"),
	Academic_Years("Academic Years"),
	Academic_Terms("Academic Terms"),
	School_Category("School Category"),
	Schools("Schools"),
	Classes("Classes"),
	Subject_Category("Subject Category"),
	Subjects("Subjects"),
	Teachers("Teachers"),
	Teacher_Tasks("Teacher Tasks"),
	Time_Tables("Time Tables"),
	School_Materials("School Materials"),
	Staff_Enrollment("Staff Enrollment"),
	Learner_Enrollment("Learner Enrollment"),
	School_Clockin("Clockin-out on Site"),
	Task_Attendence("Task Attendence"),
	School_Performance("School Performance"),
	Learner_Attendance("Learner AttendanceStatus"),
	SMC("Learner AttendanceStatus"),
	District_Performance("District Performance"),
	National_Performance("National Performance");


	private String permission;

	SystemPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public static SystemPermission getSystemPermission(String permission) {
		for (SystemPermission systemPermission : SystemPermission.values()) {
			if (systemPermission.getPermission().equalsIgnoreCase(permission)) {
				return systemPermission;
			}
		}
		return null;
	}

	public static String getSystemViewValue(String permission) {
		int i = 0;
		for (SystemPermission systemPermission : SystemPermission.values()) {
			if (systemPermission.getPermission().equalsIgnoreCase(permission)) {
				return Integer.toString(i);
			}
			i++;
		}
		return null;
	}

}
