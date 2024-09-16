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
//    private boolean hasStreams;
//    private boolean classLevel;
    private String parentSchoolClassId;
//    private String parentSchoolClassName;
}
