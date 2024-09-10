package com.planetsystems.tela.api.ClockInOutConsumer.dto.enums;

import java.util.Arrays;
import java.util.Optional;

public enum SchoolLevel {

	PRIMARY("Primary"), SECONDARY("Secondary"), CAI("Certificate Awarding Institutions");

	private String level;

	SchoolLevel(String level) {
		this.level = level;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public static SchoolLevel getSchoolLevel(String level) {
		for (SchoolLevel schoolLevel : SchoolLevel.values()) {
			if (schoolLevel.getLevel().equalsIgnoreCase(level)) {
				return schoolLevel;
			}
		}
		return null;
	}
//	public static SchoolLevel getSchoolLevel(String level) {
//		for (SchoolLevel schoolLevel : SchoolLevel.values()) {
//			if (schoolLevel.getLevel().equalsIgnoreCase(level)) {
//				return schoolLevel;
//			}
//		}
//		Optional<SchoolLevel> optional = Arrays.stream(SchoolLevel.values()).parallel()
//				.filter(schoolLevel -> schoolLevel.getLevel().equalsIgnoreCase(level)).findFirst();
//
//		return optional.isPresent() ? optional.get() : null;
//	}


}
