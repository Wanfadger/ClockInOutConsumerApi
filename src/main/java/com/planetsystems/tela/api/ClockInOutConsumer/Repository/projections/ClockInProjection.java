package com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.ClockedStatus;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ClockInProjection{
    String getId();
    LocalDate getClockInDate();
    LocalTime getClockInTime();
    ClockedStatus getClockedStatus();
    String getClockinType();
    String getComment();
    LocalDateTime getCreatedDateTime();
    int getDisplacement();
    String getLatitude();
    String getLongitude();
    String getStaffId();
    Status getStatus();
}
