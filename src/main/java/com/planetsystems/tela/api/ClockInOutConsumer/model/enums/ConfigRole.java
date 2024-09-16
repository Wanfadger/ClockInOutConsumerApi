package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum ConfigRole {

	ROLE_USER("user"),ADMIN_USER("admin");

	private String userRole;

	ConfigRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public static ConfigRole getUserRole(String role) {
		for (ConfigRole userRole : ConfigRole.values()) {
			if (userRole.getUserRole().equalsIgnoreCase(role)) {
				return userRole;
			}
		}
		return null;
	}

	public static String getUserRoleValue(String role) {
		int i = 0;
		for (ConfigRole userRole : ConfigRole.values()) {
			if (userRole.getUserRole().equalsIgnoreCase(role)) {
				return Integer.toString(i);
			}
			i++;
		}
		return null;
	}

}
