package com.demo.stratus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(Long flightId) {
        super("Flight not found with id: " + flightId);
    }
}