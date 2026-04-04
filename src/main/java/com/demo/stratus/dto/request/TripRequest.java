package com.demo.stratus.dto.request;

import com.demo.stratus.enums.TripPurpose;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TripRequest {

    @NotBlank(message = "Trip name is required")
    private String name;

    @NotNull(message = "Trip purpose is required")
    private TripPurpose purpose;

    @Min(value = 0, message = "Accompanied members cannot be negative")
    private Integer accompaniedMembers;
}