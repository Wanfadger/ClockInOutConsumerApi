package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SupervisionStatus { 
    TAUGHT("Taught in time"),
    NOT_TAUGHT("Not Taught"),
    BEYOND_TIME("Taught beyond of time"),
    OUT_OF_TIME("Taught out of time");

    private String supervision;


    public static Optional<SupervisionStatus> fromString(String supervisionStr){
        return Arrays.stream(SupervisionStatus.values()).parallel().filter(supervisionStatus ->  supervisionStatus.supervision.equalsIgnoreCase(supervisionStr)).findFirst();
    }
}
