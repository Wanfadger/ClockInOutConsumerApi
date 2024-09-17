package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LearnerAttendanceDTO {
    private String classId;
    private String attendanceDate;
    private AttendanceDTO general;
    private AttendanceDTO specialNeeds;

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Builder
   public static class AttendanceDTO{
        private long girlsPresent;
        private long boysPresent;
        private long boysAbsent;
        private long girlsAbsent;
        private String comment;
        private String staffId;
    }

}


