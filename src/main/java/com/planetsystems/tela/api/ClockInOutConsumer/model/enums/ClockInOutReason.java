package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum ClockInOutReason {
    NORMAL_END("Normal End"), MEETING("Meeting"), SICK_LEAVE("Sick leave"), WORKSHOP("Workshops"), OTHER_SCHOOL_ACTIVITIES("Other school Activities");

    private String reason;

    ClockInOutReason(String reason) {
        this.reason = reason;
    }
}
