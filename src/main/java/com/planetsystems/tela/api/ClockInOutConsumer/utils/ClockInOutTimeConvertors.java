package com.planetsystems.tela.api.ClockInOutConsumer.utils;

import java.time.LocalTime;

public class ClockInOutTimeConvertors {
    public static LocalTime convertClockOutTimeTo24Hours2(LocalTime clockOutTime) {
        return    switch (clockOutTime.getHour()){
            case 1 -> clockOutTime.withHour(13);
            case 2 -> clockOutTime.withHour(14);
            case 3 -> clockOutTime.withHour(15);
            case 4 -> clockOutTime.withHour(16);
            case 5 -> clockOutTime.withHour(17);
            case 6 -> clockOutTime.withHour(18);
            case 7 -> clockOutTime.withHour(19);
            case 8 -> clockOutTime.withHour(20);
            case 9 -> clockOutTime.withHour(21);
            case 10 -> clockOutTime.withHour(22);
            case 11 -> clockOutTime.withHour(23);
            default -> clockOutTime;
        };
    }

    public static LocalTime convertClockInTimeTo24Hours2(LocalTime clockInTime) {
        return switch (clockInTime.getHour()) {
            case 1 -> clockInTime.withHour(13);
            case 2 -> clockInTime.withHour(14);
            case 3 -> clockInTime.withHour(15);
            case 4 -> clockInTime.withHour(16);
            case 5 -> clockInTime.withHour(17);
            default -> clockInTime;

        };
    }





}
