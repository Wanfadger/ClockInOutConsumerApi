package com.planetsystems.tela.api.ClockInOutConsumer.dto.supervision;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDailyAttendanceTaskSupervisionDTO {
    String id;
    String lessonTaskId;
    String timeAttendanceId;
    String supervisorId;
//    String supervisionStatus; /// removed
    String supervisionDate;
    String supervisionTime;
    String timeStatus;// taught or not taught
    String supervisionComment;
}
