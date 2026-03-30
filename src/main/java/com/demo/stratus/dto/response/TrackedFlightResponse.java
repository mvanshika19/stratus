package com.demo.stratus.dto.response;

import com.demo.stratus.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TrackedFlightResponse {
    private Long id;
    private Long tripId;
    private String flightNumber;
    private String originIata;
    private String destinationIata;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private FlightStatus status;
    private LocalDateTime lastSyncedAt;
}