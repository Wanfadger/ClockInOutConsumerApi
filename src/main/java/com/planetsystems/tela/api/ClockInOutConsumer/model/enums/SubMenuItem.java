package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

/**
 * contains the side nav sub items(links/name tokens)
 * when clicked associated presenter is loaded
 *
 *rename to SideNavItem
 */
@Deprecated
public enum SubMenuItem {
	
	Dashboard("Overall Performance"),
	AcademicYear("Assessment Periods"),
	Location("Locations"),
	Schools("Schools"),
	Subjects("Subjects"),
	Staff_Attendance("Staff Attendance"),
	Learner_Attendance("Learner Attendance"),
	Head_Teacher_Supervision("Time Attendance"),
	Staff_Daily_Task("Task Attendance"),
	Staff_Enrollement("Staff Enrollment"),
	Learner_Enrollement("Learner Enrollment"),
	TimeTable("TimeTables"),
	Users("System Users"),
	School_Performace("School Performance"),
	District_Reports("District Performance"),
	National_Reports("National Performance"),
	HT_Reports("HeadTeacher Performance"),
	SMC_Supervision("SMC Supervision"),
	SMC_Reports("SMC Performance"),
	User_Account_Requests("User Account Requests"),
	Daily_Attendace_Dashboard("Daily Attendance"),
	Login_Audits("Login Audits"),
	Email_Attachment_Downloads("Email Attachment Downloads"),
	Batch_Uploads("Batch Uploads"),
	Data_Upload_Report("Data Upload Report"),
	PRIMARY("Primary"),
	SECONDARY("Secondary"),
	CAI("Certificate Awarding"),
	GEO_COORDINATES("Geo Coordinates");
 

	private String menuItem;

	SubMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

	public String getSystemMenuItem() {
		return menuItem;
	}

	public void setSystemMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

	public static SubMenuItem getSystemMenuItem(String menuItem) {
		for (SubMenuItem systemMenuItem : SubMenuItem.values()) {
			if (systemMenuItem.getSystemMenuItem().equalsIgnoreCase(menuItem)) {
				return systemMenuItem;
			}
		}
		return null;
	}


  
}
