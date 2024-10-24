package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum StaffType {

	TEACHER("Teacher"),//0
	HEADTEACHER("Head teacher"),//1
	DEPUTY_HEADTEACHER("Deputy head teacher"),//2
    SMC("Smc"),//3
    PRINCIPAL("Principal"),//4
    BURSAR("Bursar"),//5
    LIBRARIAN("Librarian"),//6
    LAB_TECH("Lab technician"),//7
    OTHERS("Others");//8

	private String type;



	public String getStaffType() {
		return type;
	}


	public static Optional<StaffType> fromString(String type) {
	return Arrays.stream(StaffType.values()).parallel().filter(staffType -> type.equalsIgnoreCase(staffType.type)).findFirst();
	}

}
