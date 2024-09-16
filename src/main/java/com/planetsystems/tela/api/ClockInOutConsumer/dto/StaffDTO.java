package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDTO {
    private String id;
    private String nationalId;
    private String employeeNumber;
    private String firstName;
    private String lastName;

    private boolean licensed;
    private String phoneNumber;
    private String email;

    private String initials;
    private String gender;
    private String dob;
    private String role;

    private String onPayRoll;
    private boolean teachingStaff;

//    private String registrationNo;
//    private String nationality;

    private String fileDownloadUris;
    private String hasSpecialNeeds;
    private String staffType;

    private int expectedHours; // expected number of hours per week
}
