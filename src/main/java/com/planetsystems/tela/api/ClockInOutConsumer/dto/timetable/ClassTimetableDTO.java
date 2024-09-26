package com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ClassTimetableDTO {
    private String id;
    private String classId;
    private String breakStartTime;
    private String breakEndTime;
    private String lunchStartTime;
    private String lunchEndTime;
    private String classStartTime;
    private String classEndTime;
    private int duration;
    private List<TimeTableLessonDTO> lessons;
}
