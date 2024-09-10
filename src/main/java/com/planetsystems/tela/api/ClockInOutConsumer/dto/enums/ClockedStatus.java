package com.planetsystems.tela.api.ClockInOutConsumer.dto.enums;

public enum ClockedStatus {
    CLOCKED_IN("Clocked In"),
    CLOCKED_OUT("Clocked Out");

    private String Clocked;

    ClockedStatus(String clocked) {
        Clocked = clocked;
    }

    public String getClocked() {
        return Clocked;
    }

    public void setClocked(String clocked) {
        Clocked = clocked;
    }

    public static ClockedStatus getClocked(String clocked){
        for (ClockedStatus clockedStatus : ClockedStatus.values()) {
            if (clockedStatus.getClocked().equalsIgnoreCase(clocked)) {
                return clockedStatus;
            }
        }
        return null;
    }
}