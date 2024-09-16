package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum SubjectClassification {

	Primary("Primary"), Secondary("Secondary"), CAI("Certificate Awarding Intitution"), NA("NA");

	private String category;

	SubjectClassification(String category) {
		this.category = category;
	}

	public String getSubjectClassification() {
		return category;
	}

	public void setSubjectClassification(String category) {
		this.category = category;
	}

	public static SubjectClassification getSubjectClassification(String cat) {
		for (SubjectClassification category : SubjectClassification.values()) {
			if (category.getSubjectClassification().equalsIgnoreCase(cat)) {
				return category;
			}
		}
		return null;
	}

}
