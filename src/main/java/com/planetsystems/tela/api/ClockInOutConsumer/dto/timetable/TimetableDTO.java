package com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimetableDTO {
 private String id = "";
 private String academicTermId = "";
 private String schoolId = "";
 private List<ClassTimetableDTO> classTimetables = new ArrayList<>();
}
