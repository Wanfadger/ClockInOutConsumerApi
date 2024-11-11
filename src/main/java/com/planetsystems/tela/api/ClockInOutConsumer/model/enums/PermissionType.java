package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum PermissionType {

	VIEW_PERMISION("View Permission"),
	TASK_PERMISION("Task Permission"),;

	private String type;

	PermissionType(String type) {
		this.type = type;
	}

	public String getPermissionType() {
		return type;
	}

	public void setPermissionType(String type) {
		this.type = type;
	}

	public static PermissionType getPermissionType(String type) {
		for (PermissionType permissionType : PermissionType.values()) {
			if (permissionType.getPermissionType().equalsIgnoreCase(type)) {
				return permissionType;
			}
		}
		return null;
	}

}
