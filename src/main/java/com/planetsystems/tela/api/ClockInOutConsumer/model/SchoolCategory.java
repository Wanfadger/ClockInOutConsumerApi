package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;




//school foundation body  i.e. community based, govt, catholic, COU
@Entity
@Table(name="SchoolCategories")
@NoArgsConstructor
@Setter
@Getter
public class SchoolCategory extends ParentEntity  {

    private String code;
    private String name;


    @OneToMany(mappedBy = "schoolCategory" , fetch = FetchType.LAZY)
    private Set<School> schools = new HashSet<School>();


    public SchoolCategory(String id) {
        super(id);
    }

}
