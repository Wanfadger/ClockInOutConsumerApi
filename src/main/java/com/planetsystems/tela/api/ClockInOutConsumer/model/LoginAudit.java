package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;


//@Entity
//@Table(name = "LoginAudits")
//@Cache(region = "loginAuditCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "LoginAudits")
public class LoginAudit extends ParentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUser systemUser;

	private String username;
	
	private LocalDateTime loginTime;

	public LoginAudit() {

	}

	public LoginAudit(String username, LocalDateTime loginTime) {
		this.username = username;
		this.loginTime = loginTime;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
