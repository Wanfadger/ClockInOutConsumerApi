package com.planetsystems.tela.api.ClockInOutConsumer.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoCoordinateDTO {
    double latitude;
    double longitude;
    boolean geoFenceActivated= true;
    boolean pinClockActivated = false;
    double maxDisplacement = 250.0;
}
