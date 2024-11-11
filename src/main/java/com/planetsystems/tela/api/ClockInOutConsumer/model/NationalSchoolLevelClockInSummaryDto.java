package com.planetsystems.tela.api.ClockInOutConsumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NationalSchoolLevelClockInSummaryDto implements Serializable {

    long primaryTeachersClockedIn;
    long primaryHeadteachersClockedIn;
    double primaryTeachersClockedInPercent;
    double primaryHeadteachersClockedInPercent;

    long primaryMaleTeachersClockedIn;
    long primaryFemaleTeachersClockedIn;


    long secondaryTeachersClockedIn;
    long secondaryHeadteachersClockedIn;
    double secondaryTeachersClockedInPercent;
    double secondaryHeadteachersClockedInPercent;

    long secondaryMaleTeachersClockedIn;
    long secondaryFemaleTeachersClockedIn;

    long caiTeachersClockedIn;
    long caiHeadteachersClockedIn;
    double caiTeachersClockedInPercent;
    double caiHeadteachersClockedInPercent;

    long caiMaleTeachersClockedIn;
    long caiFemaleTeachersClockedIn;
}
