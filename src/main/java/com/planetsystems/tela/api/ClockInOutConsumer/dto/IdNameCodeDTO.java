package com.planetsystems.tela.api.ClockInOutConsumer.dto;


import java.io.Serializable;


public record IdNameCodeDTO(String id , String name , String code) implements Serializable { }
