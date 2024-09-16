package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;



import com.planetsystems.tela.api.ClockInOutConsumer.exception.InvalidTokenException;

import java.util.Arrays;

public enum LessonDay {
    MON("Monday") , TUE("Tuesday") , WED("Wednesday") , THURS("Thursday") ,
    FRI("Friday") , SAT("Saturday") , SUN("Sunday");

    private String day;

    LessonDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public static LessonDay getLessonDay(String day){
//        for (LessonDay lessonDay : LessonDay.values()) {
//            if (lessonDay.getDay().equalsIgnoreCase(day)) {
//                return lessonDay;
//            }
//        }
        return Arrays.stream(LessonDay.values()).parallel().filter(lessonDay -> lessonDay.getDay().equalsIgnoreCase(day)).findFirst().orElseThrow(() -> new InvalidTokenException("Invalid LessonDay"));
    }

    public static String getLessonDayValue(String day){
        for (LessonDay lessonDay : LessonDay.values()) {
            if (lessonDay.getDay().equalsIgnoreCase(day)) {
                return lessonDay.getDay();
            }
        }
        return null;
    }



}
