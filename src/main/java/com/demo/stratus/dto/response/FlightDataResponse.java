package com.demo.stratus.dto.response;

import lombok.Data;

@Data
public class FlightDataResponse {
    private String airlineName;
    private String originIata;
    private String destinationIata;
    private String scheduledDeparture;
    private String scheduledArrival;
    private String actualDeparture;
    private String actualArrival;
    private Integer departureDelay;
    private Integer arrivalDelay;
    private String status;
}