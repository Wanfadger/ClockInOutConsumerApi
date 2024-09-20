package com.planetsystems.tela.api.ClockInOutConsumer.dto.schoolData;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class P_LearnerHeadCountDTO {
    String classId;
    String learnerType;
    long totalFemale;
    long totalMale;
    String staffId;
    String submissionDate;
}
