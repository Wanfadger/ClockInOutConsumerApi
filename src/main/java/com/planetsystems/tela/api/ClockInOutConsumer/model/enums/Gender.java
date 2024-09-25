package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE("Male"),FEMALE("Female"),OTHERS("Others");

    private String gender;

    public static Optional<Gender> fromString(String genderStr) {
        return Arrays.stream(Gender.values()).parallel().filter(g -> genderStr.equalsIgnoreCase(g.getGender())).findFirst();
    }


}
