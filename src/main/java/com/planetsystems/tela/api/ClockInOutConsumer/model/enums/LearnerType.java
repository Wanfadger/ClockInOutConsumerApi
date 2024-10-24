package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum LearnerType{
    GENERAL("General"),SPECIAL_NEEDS("Special Needs");
    String type;

    public static Optional<LearnerType> fromString(String typeStr){
        return Arrays.stream(LearnerType.values()).parallel().filter(learnerType -> learnerType.type.equalsIgnoreCase(typeStr)).findFirst();
    }

}
