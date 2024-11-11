package com.planetsystems.tela.api.ClockInOutConsumer.model;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.*;
import java.util.*;

@Entity
@Table(name="SubjectCategories")
public class SubjectCategory extends ParentEntity implements Serializable {

	private String code;
    private String name;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "subjectCategory" , fetch = FetchType.LAZY)
    private Set<Subject> subjects;

    public SubjectCategory() {
    }

    public SubjectCategory(String id) {
        super(id);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}
