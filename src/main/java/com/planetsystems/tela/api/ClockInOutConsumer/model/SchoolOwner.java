package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class SchoolOwner extends ParentEntity{ ;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY , targetEntity = SystemUserGroup.class)
    @JoinColumn(nullable = false)
    private SystemUserGroup userGroup;

    @ManyToOne(fetch = FetchType.LAZY , targetEntity = School.class)
    @JoinColumn(nullable = false)
    private School school;
}
