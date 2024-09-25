package com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TimeTableLessonDTO {
    private String id;
    private String startTime;
    private String endTime;
    private String lessonDay;
    private String subjectId;
    private String staffId;
}
