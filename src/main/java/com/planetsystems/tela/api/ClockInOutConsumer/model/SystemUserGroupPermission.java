package com.planetsystems.tela.api.ClockInOutConsumer.model;


import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.PermissionType;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SystemPermission;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;
@Entity
@Table(name="SystemUserGroupPermissions")
@Deprecated
public class SystemUserGroupPermission extends ParentEntity {

	/**
	 */
	private static final long serialVersionUID = 1L;

//	@ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.REFRESH)
//	private SystemUserGroup systemUserGroup;

	private SystemPermission systemPermission;

	private PermissionType permissionType;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUser createdBy;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUser updatedBy;

	public SystemUserGroupPermission() {

	}

	public SystemPermission getSystemPermission() {
		return systemPermission;
	}

	public void setSystemPermission(SystemPermission systemPermission) {
		this.systemPermission = systemPermission;
	}


	public SystemUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SystemUser createdBy) {
		this.createdBy = createdBy;
	}


	public SystemUser getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(SystemUser updatedBy) {
		this.updatedBy = updatedBy;
	}

	public PermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(PermissionType permissionType) {
		this.permissionType = permissionType;
	}

//	public SystemUserGroup getSystemUserGroup() {
//		return systemUserGroup;
//	}
//
//	public void setSystemUserGroup(SystemUserGroup systemUserGroup) {
//		this.systemUserGroup = systemUserGroup;
//	}

}
