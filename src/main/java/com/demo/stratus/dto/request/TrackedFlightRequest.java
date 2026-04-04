package com.demo.stratus.dto.request;

import com.demo.stratus.enums.CabinClass;
import com.demo.stratus.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TrackedFlightRequest {

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @Size(max = 10, message = "Seat number must not exceed 10 characters")
    private String seatNumber;

    private SeatType seatType;

    private CabinClass cabinClass;

    @Size(max = 20, message = "Reference number must not exceed 20 characters")
    private String referenceNumber;
}