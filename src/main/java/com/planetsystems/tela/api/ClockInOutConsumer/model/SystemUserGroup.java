package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.AdministrationLevel;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;


@Entity
@Table(name="SystemUserGroups")
@Setter
@Getter
@NoArgsConstructor
public class SystemUserGroup extends ParentEntity {

	private String code;
	@Column(unique = true)
	private String name;
	private String description;
	private boolean defaultGroup = false;
	private boolean receiveAlerts = false;
	private boolean administrativeRole= false;
	private AdministrationLevel userLevel;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUserProfile createdBy;
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUserProfile updatedBy;

	@OneToMany(mappedBy = "systemUserGroup")
	private Set<SystemUserGroupSystemMenu> systemUserGroupSystemMenus = new HashSet<>();


	public SystemUserGroup(String id) {
		super(id);
	}
}
