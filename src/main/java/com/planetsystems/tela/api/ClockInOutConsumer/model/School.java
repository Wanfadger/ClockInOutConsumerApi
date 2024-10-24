package com.planetsystems.tela.api.ClockInOutConsumer.model;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@NamedEntityGraphs({
//		@NamedEntityGraph(name = "school-graph" , attributeNodes = {
//				@NamedAttributeNode("schoolCategory"),
//				@NamedAttributeNode(value = "district", subgraph = "region-sub-graph"),
//				@NamedAttributeNode(value = "schoolClasses"),
//				@NamedAttributeNode(value = "schoolStaffs"),
//				@NamedAttributeNode(value = "timeAttendanceSupervisions")},
//				subgraphs = {
//						@NamedSubgraph(name = "region-sub-graph", attributeNodes = @NamedAttributeNode(value = "region"))
//				}
//		),
//		@NamedEntityGraph(name = "partial-school-graph" , attributeNodes = {
//				@NamedAttributeNode("schoolLevel"),
//				@NamedAttributeNode(value = "schoolOwnership"),
//				 }
//		),
//
//})

@NamedEntityGraph(name = "school-district-graph", attributeNodes = {
        @NamedAttributeNode("district"),
        @NamedAttributeNode("schoolCategory")
})

@NamedEntityGraph(name = "school-district-region-graph", attributeNodes = {@NamedAttributeNode("district")})


@Entity
@Table(name = "Schools")
@Setter
@Getter
@NoArgsConstructor
public class School extends ParentEntity implements Serializable {
    private String code;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolCategory_id")
    private SchoolCategory schoolCategory; // school foundation body i.e. community based, govt, catholic, COU

    public String latitude;
    public String longitude;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SchoolClass> schoolClasses = new HashSet<>();

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SchoolStaff> schoolStaffs = new HashSet<>();

//	@OneToOne(mappedBy = "school" , fetch = FetchType.LAZY)
//	private TimeTable timeTable;

//	@OneToOne(mappedBy = "school")
//	private ClockIn clockIn;

    @OneToMany(mappedBy = "school")
    private Set<TimeAttendanceSupervision> timeAttendanceSupervisions = new HashSet<>();

    private String deviceNumber; // Phone Serial number

    private Status activationStatus = Status.IN_ACTIVE;

    private SchoolLevel schoolLevel;
    private SchoolOwnership schoolOwnership;
    private SchoolType schoolType;
    private SchoolGenderCategory schoolGenderCategory;

    private RolloutPhase rolloutPhase;

    private String telaSchoolNumber; //phone number

    private String emisNumber;
    private String telaSchoolUID;

    //private boolean attendanceTracked = true;
    private Boolean attendanceTracked;
    private Boolean licensed;



    @OneToMany(mappedBy = "school" , targetEntity = SchoolOwner.class)
    List<SchoolOwner> schoolOwners;

    @OneToMany(mappedBy = "school" , targetEntity = SchoolGeoCoordinate.class)
    List<SchoolGeoCoordinate> schoolGeoCoordinateList;



//    @OneToOne(mappedBy = "" , targetEntity = TelaSchoolLicense.class , fetch = FetchType.LAZY)
//    private TelaSchoolLicense telaSchoolLicense;




    public School(String id) {
        super(id);
    }

}
