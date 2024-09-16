package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum ResponseType implements Serializable {
    SCHOOL("SCHOOL"),CLASSES("CLASSES"),STAFFS("STAFFS"),CLOCKINS("CLOCKINS"), CLOCKIN("CLOCKIN"), SUBJECTS("SUBJECTS"), LEARNER_HEADCOUNTS("LEARNER_HEADCOUNTS");

        String type;
}
