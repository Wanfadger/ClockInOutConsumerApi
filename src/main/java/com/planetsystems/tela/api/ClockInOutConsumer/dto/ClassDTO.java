package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassDTO {
    private String id;
    private String name;
    private int localId;
    private String parentSchoolClassId;
}
