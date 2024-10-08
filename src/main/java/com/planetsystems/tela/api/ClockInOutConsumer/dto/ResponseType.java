package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum ResponseType implements Serializable {
    SCHOOL("SCHOOL"),CLASSES("CLASSES"),STAFFS("STAFFS"),CLOCKINS("CLOCKINS"),CLOCKOUTS("CLOCKOUTS"), SUBJECTS("SUBJECTS"), LEARNER_HEADCOUNTS("LEARNER_HEADCOUNTS"),
    LEARNER_ATTENDANCES("LEARNER_ATTENDANCES"), DISTRICTS("DISTRICTS"), STAFF_DAILY_TIME_ATTENDANCES("STAFF_DAILY_TIME_ATTENDANCES"),
    SCHOOL_TIMETABLE("SCHOOL_TIMETABLE");

        String type;
}
