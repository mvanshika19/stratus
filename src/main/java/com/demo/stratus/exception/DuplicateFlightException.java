package com.demo.stratus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateFlightException extends RuntimeException {
    public DuplicateFlightException(String flightNumber) {
        super("Flight " + flightNumber + " is already tracked in this trip");
    }
}