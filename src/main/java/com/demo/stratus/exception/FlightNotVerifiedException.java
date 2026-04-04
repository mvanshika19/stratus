package com.demo.stratus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FlightNotVerifiedException extends RuntimeException {
    public FlightNotVerifiedException(String flightNumber) {
        super("Flight " + flightNumber + " could not be verified. Please check the flight number and try again.");
    }
}