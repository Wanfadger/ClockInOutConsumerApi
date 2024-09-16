package com.planetsystems.tela.api.ClockInOutConsumer.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemAudit {
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Id
    private String id;
    private String resource;
    private String email;
    private String requestType;


    @Column(length = 1024)
    private String requestDetails;

    @CreationTimestamp()
    private LocalDateTime createdDateTime;



    public SystemAudit(String resource, String email, String requestType, String requestDetails) {
        this.resource = resource;
        this.email = email;
        this.requestType = requestType;
        this.requestDetails = requestDetails;
    }
}
