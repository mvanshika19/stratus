package com.demo.stratus.controller;

import com.demo.stratus.dto.request.TrackedFlightRequest;
import com.demo.stratus.dto.response.TrackedFlightResponse;
import com.demo.stratus.service.TrackedFlightService;
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
@Tag(name = "Tracked Flights", description = "Flight tracking endpoints")
public class TrackedFlightController {

    private final TrackedFlightService trackedFlightService;

    @PostMapping("/{tripId}/flights")
    @Operation(summary = "Add a flight to a trip")
    public ResponseEntity<TrackedFlightResponse> addFlightToTrip(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long tripId,
            @Valid @RequestBody TrackedFlightRequest request) {
        log.info("Add flight request for trip {} by user: {}",
                tripId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trackedFlightService.addFlightToTrip(
                        userDetails.getUsername(), tripId, request));
    }

    @GetMapping("/{tripId}/flights")
    @Operation(summary = "Get all flights for a trip")
    public ResponseEntity<List<TrackedFlightResponse>> getAllFlightsForTrip(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long tripId) {
        log.info("Get all flights request for trip {} by user: {}",
                tripId, userDetails.getUsername());
        return ResponseEntity.ok(trackedFlightService.getAllFlightsForTrip(
                userDetails.getUsername(), tripId));
    }

    @GetMapping("/{tripId}/flights/{flightId}")
    @Operation(summary = "Get a flight by ID")
    public ResponseEntity<TrackedFlightResponse> getFlightById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long tripId,
            @PathVariable Long flightId) {
        log.info("Get flight {} request for trip {} by user: {}",
                flightId, tripId, userDetails.getUsername());
        return ResponseEntity.ok(trackedFlightService.getFlightById(
                userDetails.getUsername(), tripId, flightId));
    }

    @DeleteMapping("/{tripId}/flights/{flightId}")
    @Operation(summary = "Remove a flight from a trip")
    public ResponseEntity<Void> deleteFlightFromTrip(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long tripId,
            @PathVariable Long flightId) {
        log.info("Delete flight {} request for trip {} by user: {}",
                flightId, tripId, userDetails.getUsername());
        trackedFlightService.deleteFlightFromTrip(
                userDetails.getUsername(), tripId, flightId);
        return ResponseEntity.noContent().build();
    }
}