package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

//@NamedEntityGraph(name = "systemUserGroup-systemMenu-detail-graph" , attributeNodes = {
//		@NamedAttributeNode(value = "systemUserGroup"),
//		@NamedAttributeNode(value = "systemMenu"),
//		@NamedAttributeNode(value = "createdBy"),
//		@NamedAttributeNode(value = "updatedBy"),
//})
@Entity
@Table(name="SystemUserGroupSystemMenus")
//@Cache(region = "SystemUserGroupSystemMenuCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Setter
@Getter
@NoArgsConstructor
public class SystemUserGroupSystemMenu extends ParentEntity {

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUserGroup systemUserGroup;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemMenu systemMenu;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUserProfile createdBy;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUserProfile updatedBy;

	public SystemUserGroupSystemMenu(SystemUserGroup systemUserGroup, SystemMenu systemMenu) {
		this.systemUserGroup = systemUserGroup;
		this.systemMenu = systemMenu;
	}
}
