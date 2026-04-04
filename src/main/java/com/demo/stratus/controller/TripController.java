package com.demo.stratus.controller;

import com.demo.stratus.dto.request.TripRequest;
import com.demo.stratus.dto.response.TripResponse;
import com.demo.stratus.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
@Tag(name = "Trips", description = "Trip management endpoints")
public class TripController {

    private final TripService tripService;

    @PostMapping
    @Operation(summary = "Create a new trip")
    public ResponseEntity<TripResponse> createTrip(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TripRequest request) {
        log.info("Create trip request for: {}", userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tripService.createTrip(userDetails.getUsername(), request));
    }

    @GetMapping
    @Operation(summary = "Get all trips for current user")
    public ResponseEntity<List<TripResponse>> getAllTrips(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Get all trips request for: {}", userDetails.getUsername());
        return ResponseEntity.ok(tripService.getAllTrips(userDetails.getUsername()));
    }

    @GetMapping("/{tripId}")
    @Operation(summary = "Get a trip by ID")
    public ResponseEntity<TripResponse> getTripById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long tripId) {
        log.info("Get trip {} request for: {}", tripId, userDetails.getUsername());
        return ResponseEntity.ok(tripService.getTripById(userDetails.getUsername(), tripId));
    }

    @PutMapping("/{tripId}")
    @Operation(summary = "Update a trip")
    public ResponseEntity<TripResponse> updateTrip(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long tripId,
            @Valid @RequestBody TripRequest request) {
        log.info("Update trip {} request for: {}", tripId, userDetails.getUsername());
        return ResponseEntity.ok(tripService.updateTrip(userDetails.getUsername(), tripId, request));
    }

    @DeleteMapping("/{tripId}")
    @Operation(summary = "Delete a trip")
    public ResponseEntity<Void> deleteTrip(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long tripId) {
        log.info("Delete trip {} request for: {}", tripId, userDetails.getUsername());
        tripService.deleteTrip(userDetails.getUsername(), tripId);
        return ResponseEntity.noContent().build();
    }
}