package com.planetsystems.tela.api.ClockInOutConsumer.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@NamedEntityGraph(name = "systemUser-profile-school-detail-graph" , attributeNodes = {
		@NamedAttributeNode(value = "systemUserProfile"),
		@NamedAttributeNode(value = "school", subgraph = "school-sub-graph")
} ,
subgraphs = {
		@NamedSubgraph(name = "school-sub-graph" , attributeNodes = {
				@NamedAttributeNode(value = "schoolCategory"),
				@NamedAttributeNode(value = "district")
		})
}
)
@Entity
@Table(name="SystemUserProfileSchools")
public class SystemUserProfileSchool extends ParentEntity{

    @OneToOne(fetch = FetchType.LAZY)
    private SystemUserProfile systemUserProfile;

    @OneToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY , targetEntity = School.class)
    private School school;

    public SystemUserProfileSchool() {
    }

    public SystemUserProfileSchool(String id) {
        super(id);
    }

    public SystemUserProfile getSystemUserProfile() {
        return systemUserProfile;
    }

    public void setSystemUserProfile(SystemUserProfile systemUserProfile) {
        this.systemUserProfile = systemUserProfile;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

}
