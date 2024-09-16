package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LearnerHeadCountDTO {
    IdNameDTO schoolClass;
    String submissionDate;
    GenderCountDTO general;
    GenderCountDTO specialNeeds;
}

