package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;



import com.planetsystems.tela.api.ClockInOutConsumer.exception.InvalidTokenException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LessonDay {
    MON("Monday") , TUE("Tuesday") , WED("Wednesday") , THURS("Thursday") ,
    FRI("Friday") , SAT("Saturday") , SUN("Sunday");

    private String day;

    public static Optional<LessonDay> fromString(String dayStr){
        return Arrays.stream(LessonDay.values()).parallel().filter(lessonDay -> lessonDay.day.equalsIgnoreCase(dayStr)).findFirst();
    }



}
