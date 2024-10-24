package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GeoCoordinateDTO {
    private String id;
    double latitude;
    double longitude;
    boolean geoFenceActivated= true;
    boolean pinClockActivated = false;
    double maxDisplacement = 250.0;
}
