package com.demo.stratus.dto.response;

import com.demo.stratus.enums.CabinClass;
import com.demo.stratus.enums.FlightStatus;
import com.demo.stratus.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TrackedFlightResponse {
    private Long id;
    private Long tripId;
    private String flightNumber;
    private String airlineName;
    private String originIata;
    private String destinationIata;
    private String seatNumber;
    private SeatType seatType;
    private CabinClass cabinClass;
    private String referenceNumber;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private LocalDateTime actualDeparture;
    private LocalDateTime actualArrival;
    private Integer departureDelayMinutes;
    private Integer arrivalDelayMinutes;
    private FlightStatus status;
    private LocalDateTime lastSyncedAt;
}