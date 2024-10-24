package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LearnerHeadCountDTO {
    String classId;
    String learnerType;
    long totalFemale;
    long totalMale;
    String staffId;
    String submissionDate;
}

