package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum ClockedStatus {
    CLOCKED_IN("Clocked In"),
    CLOCKED_OUT("Clocked Out");

    private String clocked;


    public static Optional<ClockedStatus> fromString(String clockedStr){
        return Arrays.stream(ClockedStatus.values()).parallel().filter(clockedStatus -> clockedStatus.clocked.equalsIgnoreCase(clockedStr)).findFirst();
    }
}
