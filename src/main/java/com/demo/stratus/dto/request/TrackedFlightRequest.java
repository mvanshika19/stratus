package com.demo.stratus.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrackedFlightRequest {

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Origin IATA code is required")
    @Size(min = 3, max = 3, message = "Origin IATA must be 3 characters")
    @Pattern(regexp = "[A-Z]{3}", message = "Origin IATA must be 3 uppercase letters")
    private String originIata;

    @NotBlank(message = "Destination IATA code is required")
    @Size(min = 3, max = 3, message = "Destination IATA must be 3 characters")
    @Pattern(regexp = "[A-Z]{3}", message = "Destination IATA must be 3 uppercase letters")
    private String destinationIata;

    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
}