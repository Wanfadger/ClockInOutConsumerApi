package com.planetsystems.tela.api.ClockInOutConsumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NationalSchoolAttendanceSummaryDto implements Serializable {

    private List<NationalSchoolAttendanceDetailsDto> primary;
    private List<NationalSchoolAttendanceDetailsDto> secondary;
    private List<NationalSchoolAttendanceDetailsDto> cai;
}
