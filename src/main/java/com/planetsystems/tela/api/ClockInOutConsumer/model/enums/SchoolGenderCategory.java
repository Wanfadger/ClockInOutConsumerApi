package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum SchoolGenderCategory {
  
	MIXED("Mixed"), SINGLE_BOYS("Single Boys"), SINGLE_GIRLS("Single Girls");

	private String category;

	SchoolGenderCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public static SchoolGenderCategory getSchoolGenderCategory(String category) {
		for (SchoolGenderCategory genderCategory : SchoolGenderCategory.values()) {
			if (genderCategory.getCategory().equalsIgnoreCase(category)) {
				return genderCategory;
			}
		}
		return null;
	}


}
