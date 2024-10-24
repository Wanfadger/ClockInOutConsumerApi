package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AttendanceStatus {
    PRESENT("Present") , ABSENT("Absent");
    private String attendance;


    public static Optional<AttendanceStatus> fromString(String attendanceStr){
        return Arrays.stream(AttendanceStatus.values()).parallel().filter(attendanceStatus -> attendanceStatus.attendance.equalsIgnoreCase(attendanceStr)).findFirst();
    }
}
