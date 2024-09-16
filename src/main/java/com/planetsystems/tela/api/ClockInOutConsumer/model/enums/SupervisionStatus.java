package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum SupervisionStatus { 
    TAUGHT("Taught in time"),
    NOT_TAUGHT("Not Taught"),
    BEYOND_TIME("Taught beyond of time"),
    OUT_OF_TIME("Taught out of time");

    private String supervision;

    SupervisionStatus(String supervision) {
        this.supervision = supervision;
    }

    public String getSupervision() {
        return supervision;
    }

    public void setSupervision(String supervision) {
        this.supervision = supervision;
    }

    public static SupervisionStatus getSupervision(String supervision){
        for (SupervisionStatus supervisionStatus : SupervisionStatus.values()) {
            if (supervisionStatus.getSupervision().equalsIgnoreCase(supervision)) {
                return supervisionStatus;
            }
        }
        return null;
    }
}
