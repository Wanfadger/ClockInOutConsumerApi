package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClockInTableColumns {
    private int status;
    private Local clockInDate;
    private LocalTime clockInTime;
    private int clockedStatus;
    private String comment;
    private double latitude;
    private double longitude;
    private String academicTerm_id;
    private String school_id;
    private String schoolStaff_id;
    private int clockinType;
    private int displacement;
}
