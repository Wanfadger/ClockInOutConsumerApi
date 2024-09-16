package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum FileStorageException {
    MALE("Male"),FEMALE("Female"),OTHERS("Others");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static Gender getGender(String gender) {
        for (Gender gd : Gender.values()) {
            if (gd.getGender().equalsIgnoreCase(gender)) {
                return gd;
            }
        }
        return null;
    }

    public static String getType(String gender) {
        int i = 0;
        for (Gender gender2 : Gender.values()) {
            if (gender2.getGender().equalsIgnoreCase(gender)) {
                return Integer.toString(i);
            }
            i++;
        }
        return null;
    }

}
