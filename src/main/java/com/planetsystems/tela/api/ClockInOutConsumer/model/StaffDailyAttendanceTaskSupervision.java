package com.planetsystems.tela.api.ClockInOutConsumer.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SupervisionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.*;
import java.util.*;

/**
 * recurring staff daily supervisions
 */
@Entity
@Table(name="StaffDailyAttendanceTaskSupervisions")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StaffDailyAttendanceTaskSupervision extends ParentEntity{

    /**
     * supervised attendance lessons under staffDailyAttendanceSupervision
     */
    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
    private StaffDailyTimeTableLesson staffDailyTimeTableLesson;

    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
    @JoinColumn(name = "staffDailyAttendanceSupervision_id")
    private StaffDailyAttendanceSupervision staffDailyAttendanceSupervision;

    private SupervisionStatus teachingTimeStatus;
    
//    private SupervisionStatus teachingStatus;
    
    private String comment;

}
