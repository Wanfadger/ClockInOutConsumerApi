package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDTO {
    int localId;
    String id;
    String dob;
    String role;
    String gender;
    String lastName;
    String initials;
    boolean licensed;
    String firstName;
    String staffType;
    String onPayRoll;
    String nationalId;
    String phoneNumber;
    String emailAddress;
    int expectedHours;
    String employeeNumber;
    String hasSpecialNeeds;
}
