package com.demo.stratus.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TripRequest {

    @NotBlank(message = "Trip name is required")
    private String name;
}