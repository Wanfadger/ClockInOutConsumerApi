package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDailyTimeAttendanceDTO {
    String id;

    @NotEmpty(message = "staffId is required")
    @NotBlank(message = "staffId is required")
    String staffId;

    @NotEmpty(message = "supervisorId is required")
    @NotBlank(message = "supervisorId is required")
    String supervisorId;

    @NotEmpty(message = "supervisorComment is required")
    @NotBlank(message = "supervisorComment is required")
    String supervisorComment;

    @NotEmpty(message = "supervisionStatus is required")
    @NotBlank(message = "supervisionStatus is required")
    String supervisionStatus;

    @NotEmpty(message = "supervisionDateTime is required")
    @NotBlank(message = "supervisionDateTime is required")
    String supervisionDateTime;
}
