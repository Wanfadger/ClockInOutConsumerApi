package com.planetsystems.tela.api.ClockInOutConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Otp {
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Id
    private String id;

    @Column(unique = true)
    private int code;

    @Column(unique = true)
    private String username;
    private LocalDateTime expiresAt;
    @CreationTimestamp()
    private LocalDateTime createdDateTime;

    @UpdateTimestamp()
    private LocalDateTime updatedDateTime;
}
