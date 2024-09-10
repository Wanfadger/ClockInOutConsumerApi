package com.planetsystems.tela.api.ClockInOutConsumer.dto.enums;

public enum AssessmentPeriodType {
	
	ACADEMIC_TERM("Academic Term"),
	SEMESTER("Semester"),
	DEFAULT("Default");
	
	private String type;

	AssessmentPeriodType(String type) {
		this.type = type;
	}

	public String getAssessmentPeriodType() {
		return type;
	}

	public void setAssessmentPeriodType(String type) {
		this.type = type;
	}

	public static AssessmentPeriodType getAssessmentPeriodType(String type) {
		for (AssessmentPeriodType staffType : AssessmentPeriodType.values()) {
			if (staffType.getAssessmentPeriodType().equalsIgnoreCase(type)) {
				return staffType;
			}
		}
		return null;
	}


}
