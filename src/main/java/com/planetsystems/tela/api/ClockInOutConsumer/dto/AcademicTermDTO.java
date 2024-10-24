package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcademicTermDTO implements Serializable {
    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private String year;
}
