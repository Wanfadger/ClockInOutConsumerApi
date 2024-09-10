package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MQResponseDto<T> implements Serializable {
	ResponseType responseType;
	T data;
}
