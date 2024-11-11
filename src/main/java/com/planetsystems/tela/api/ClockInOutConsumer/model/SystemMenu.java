package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.NavigationMenu;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SubMenuItem;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

/**
 * rename to SideNavMenu(SideNavTitle , SideNaveItem)
 */
@Entity
@Table(name="SystemMenus")
@Setter
@Getter
public class SystemMenu extends ParentEntity {
	private NavigationMenu navigationMenu;
	private SubMenuItem subMenuItem;

	private Status activationStatus;

	public SystemMenu() { }

	public SystemMenu(String id) {
		super(id);
	}


	public SystemMenu(NavigationMenu navigationMenu, SubMenuItem subMenuItem) {
		this.navigationMenu = navigationMenu;
		this.subMenuItem = subMenuItem;
		this.activationStatus = Status.ACTIVE;
	}

	@PrePersist()
	void onPersist(){
		this.setCreatedDateTime(LocalDateTime.now());
		this.setUpdatedDateTime(LocalDateTime.now());
		this.activationStatus = Status.ACTIVE;
	}


	@Override
	public String toString() {
		return "SystemMenu{" +
				"navigationMenu=" + navigationMenu +
				", subMenuItem=" + subMenuItem +
				", activationStatus=" + activationStatus +
				'}';
	}
}
