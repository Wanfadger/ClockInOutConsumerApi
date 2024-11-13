package com.planetsystems.tela.api.ClockInOutConsumer.model;


import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.AssessmentPeriodType;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;



@Entity(name = "AcademicTerms" )
@Table(name="AcademicTerms",indexes = {@Index(columnList = "code,term")})
@Getter
@Setter
@NoArgsConstructor
public class AcademicTerm extends ParentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String term;
    private Status activationStatus;


    private LocalDate startDate;


    private LocalDate endDate;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private AcademicYear academicYear;

    @OneToMany(mappedBy = "academicTerm" , fetch = FetchType.LAZY)
    private Set<SchoolClass> schoolClasses = new HashSet<>();

//    @OneToOne(mappedBy = "academicTerm" , fetch = FetchType.LAZY)
//    private LearnerAttendance learnerAttendance;

//    @OneToOne(mappedBy = "academicTerm" , fetch = FetchType.LAZY)
//    private TimeTable timeTable;

//    @OneToOne(mappedBy = "academicTerm" , fetch = FetchType.LAZY)
//    private ClockIn clockIn;
    
    private String displayName;
    private AssessmentPeriodType assessmentPeriodType;


    public AcademicTerm(String id) {
        super(id);
    }


}
