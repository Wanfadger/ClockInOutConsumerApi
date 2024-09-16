package com.planetsystems.tela.api.ClockInOutConsumer.model;


import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;


//@Entity
//@Table(name="GeneralUserDetails",indexes = {@Index(columnList = "id")})
//@Cache(region = "generalUserDetailCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "GeneralUserDetails")
@Setter
@Getter
@NoArgsConstructor
public class GeneralUserDetail extends ParentEntity {
	private String firstName;
	private String lastName;

	private String phoneNumber;

	private String email;

	private LocalDate dob;


	private String nationalId;
	private Gender gender;
    private String nameAbbrev;

	@OneToOne(mappedBy = "generalUserDetail" , fetch = FetchType.LAZY , targetEntity = SchoolStaff.class)
	private SchoolStaff schoolStaff;

//	@OneToOne(mappedBy = "generalUserDetail" , fetch = FetchType.LAZY)
//	private SystemUserProfile systemUserProfile;


	public GeneralUserDetail(String id) {
		super(id);
	}

}
