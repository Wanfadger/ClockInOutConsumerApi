package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum AuthAccountType {
    SCHOOL_OWNER("school owner") ,  SUPPORT("Support") , ADMIN("admin") , STAFF("staff");
    private String accountType;


    public static Optional<AuthAccountType> accountType(String accountTypeStr){
      return   Arrays.stream(AuthAccountType.values()).parallel().filter(authAccountType -> authAccountType.getAccountType().equalsIgnoreCase(accountTypeStr)).findFirst();
    }
}
