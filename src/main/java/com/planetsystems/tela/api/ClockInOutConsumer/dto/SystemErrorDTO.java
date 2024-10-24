package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class SystemErrorDTO implements Serializable {

    private String message;
    private int errorCode;
    

    public SystemErrorDTO() {
        this.message = "An error occurred while initiating transaction. Please try again";
    }


}
