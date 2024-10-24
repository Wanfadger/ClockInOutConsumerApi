package com.planetsystems.tela.api.ClockInOutConsumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NationalSchoolAttendanceDetailsDto {

    private String districtId;
    private String districtName;
    private int schoolsEnrolled;
    private int schoolsClockedIn;

    private int staffEnrolled;
    private int staffClockedIn;

    private int learnersEnrolled;
    private int learnersPresent;
}
