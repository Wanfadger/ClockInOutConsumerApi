package com.planetsystems.tela.api.ClockInOutConsumer.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "ClockInTrails")
@Table(indexes = {
        @Index(columnList = "staffCode"),
        @Index(columnList = "clockInDate"),

})
public class ClockInTrail {
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Id
    private String id;

    private String staffCode;
    private LocalDate clockInDate;

    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public ClockInTrail() {
    }

    public ClockInTrail(String staffCode, LocalDate clockInDate) {
        this.staffCode = staffCode;
        this.clockInDate = clockInDate;
        this.updatedDateTime = LocalDateTime.now();
        this.createdDateTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public LocalDate getClockInDate() {
        return clockInDate;
    }

    public void setClockInDate(LocalDate clockInDate) {
        this.clockInDate = clockInDate;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
}
