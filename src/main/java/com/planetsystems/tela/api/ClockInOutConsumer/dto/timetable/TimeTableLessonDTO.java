package com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class TimeTableLessonDTO {
    private String id;
    private String startTime;
    private String endTime;
    private String lessonDay;
    private String subjectId;
    private String staffId;
}
