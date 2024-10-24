package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;



@Entity
@Table(name="SystemUserProfiles")
public class SystemUserProfile  extends ParentEntity{

	@OneToOne(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY)
    private SystemUser systemUser;

    @ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY)
    private SystemUserGroup systemUserGroup;  // it was not right for profile to know about the group

    @OneToOne(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY)
    private GeneralUserDetail generalUserDetail;


    public SystemUserProfile() {
    }

    public SystemUserProfile(String id) {
        super(id);
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public SystemUserGroup getSystemUserGroup() {
        return systemUserGroup;
    }

    public void setSystemUserGroup(SystemUserGroup systemUserGroup) {
        this.systemUserGroup = systemUserGroup;
    }

    public GeneralUserDetail getGeneralUserDetail() {
        return generalUserDetail;
    }

    public void setGeneralUserDetail(GeneralUserDetail generalUserDetail) {
        this.generalUserDetail = generalUserDetail;
    }



}
