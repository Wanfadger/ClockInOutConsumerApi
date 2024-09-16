package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum AttendanceStatus {
    PRESENT("Present") , ABSENT("Absent");
    private String attendance;

    AttendanceStatus(String attendance) {
        this.attendance = attendance;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public static AttendanceStatus getAttendance(String attendance){
        for (AttendanceStatus attendanceStatus : AttendanceStatus.values()) {
            if (attendanceStatus.getAttendance().equalsIgnoreCase(attendance)) {
                return attendanceStatus;
            }
        }
        return null;
    }
}
