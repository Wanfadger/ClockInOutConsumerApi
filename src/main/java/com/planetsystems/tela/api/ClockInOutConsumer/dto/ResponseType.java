package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum ResponseType implements Serializable {
    CLOCKINS("clockIns");

        String type;
}
