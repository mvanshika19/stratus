package com.demo.stratus.service;

import com.demo.stratus.dto.request.TrackedFlightRequest;
import com.demo.stratus.dto.response.FlightDataResponse;
import com.demo.stratus.dto.response.TrackedFlightResponse;
import com.demo.stratus.enums.FlightStatus;
import com.demo.stratus.exception.DuplicateFlightException;
import com.demo.stratus.exception.FlightNotFoundException;
import com.demo.stratus.exception.FlightNotVerifiedException;
import com.demo.stratus.exception.TripNotFoundException;
import com.demo.stratus.exception.UnauthorizedAccessException;
import com.demo.stratus.model.TrackedFlight;
import com.demo.stratus.model.Trip;
import com.demo.stratus.repository.TrackedFlightRepository;
import com.demo.stratus.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackedFlightService {

        private final TrackedFlightRepository trackedFlightRepository;
        private final TripRepository tripRepository;
        private final AviationStackService aviationStackService;

        @Transactional
        public TrackedFlightResponse addFlightToTrip(String email, Long tripId,
                        TrackedFlightRequest request) {
                log.info("Adding flight {} to trip {} for user: {}",
                                request.getFlightNumber(), tripId, email);

                Trip trip = tripRepository.findById(tripId)
                                .orElseThrow(() -> new TripNotFoundException(tripId));

                if (!trip.getUser().getEmail().equals(email)) {
                        throw new UnauthorizedAccessException("Access denied — trip does not belong to user");
                }

                if (trackedFlightRepository.existsByTripIdAndFlightNumber(
                                tripId, request.getFlightNumber())) {
                        throw new DuplicateFlightException(request.getFlightNumber());
                }

                TrackedFlight flight = new TrackedFlight();
                flight.setTrip(trip);
                flight.setFlightNumber(request.getFlightNumber().toUpperCase());
                flight.setSeatNumber(request.getSeatNumber());
                flight.setSeatType(request.getSeatType());
                flight.setCabinClass(request.getCabinClass());
                flight.setReferenceNumber(request.getReferenceNumber());
                flight.setStatus(FlightStatus.ON_TIME);
                flight.setLastSyncedAt(LocalDateTime.now());

                FlightDataResponse flightData = aviationStackService.getFlightData(
                                request.getFlightNumber());

                if (flightData != null) {
                        populateFromAviationStack(flight, flightData);
                } else {
                        log.warn("No AviationStack data found for flight: {}", request.getFlightNumber());
                        throw new FlightNotVerifiedException(request.getFlightNumber());
                }

                trackedFlightRepository.save(flight);
                log.info("Flight {} added successfully to trip {}", request.getFlightNumber(), tripId);

                return mapToResponse(flight);
        }

        private void populateFromAviationStack(TrackedFlight flight, FlightDataResponse data) {
                flight.setAirlineName(data.getAirlineName());
                flight.setOriginIata(data.getOriginIata());
                flight.setDestinationIata(data.getDestinationIata());
                flight.setDepartureDelayMinutes(data.getDepartureDelay());
                flight.setArrivalDelayMinutes(data.getArrivalDelay());

                if (data.getScheduledDeparture() != null) {
                        flight.setScheduledDeparture(
                                        java.time.LocalDateTime.parse(
                                                        data.getScheduledDeparture()
                                                                        .replace("Z", "")
                                                                        .substring(0, 19)));
                }
                if (data.getScheduledArrival() != null) {
                        flight.setScheduledArrival(
                                        java.time.LocalDateTime.parse(
                                                        data.getScheduledArrival()
                                                                        .replace("Z", "")
                                                                        .substring(0, 19)));
                }
                if (data.getActualDeparture() != null) {
                        flight.setActualDeparture(
                                        java.time.LocalDateTime.parse(
                                                        data.getActualDeparture()
                                                                        .replace("Z", "")
                                                                        .substring(0, 19)));
                }
                if (data.getActualArrival() != null) {
                        flight.setActualArrival(
                                        java.time.LocalDateTime.parse(
                                                        data.getActualArrival()
                                                                        .replace("Z", "")
                                                                        .substring(0, 19)));
                }

                if (data.getStatus() != null) {
                        switch (data.getStatus().toLowerCase()) {
                                case "landed" -> flight.setStatus(FlightStatus.LANDED);
                                case "cancelled" -> flight.setStatus(FlightStatus.CANCELLED);
                                case "delayed" -> flight.setStatus(FlightStatus.DELAYED);
                                default -> flight.setStatus(FlightStatus.ON_TIME);
                        }
                }
        }

        @Transactional
        public List<TrackedFlightResponse> getAllFlightsForTrip(String email, Long tripId) {
                log.info("Fetching all flights for trip {} for user: {}", tripId, email);
                Trip trip = tripRepository.findById(tripId)
                                .orElseThrow(() -> new TripNotFoundException(tripId));
                validateTripOwnership(trip, email);
                return trackedFlightRepository.findByTripId(tripId)
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        @Transactional
        public TrackedFlightResponse getFlightById(String email, Long tripId, Long flightId) {
                log.info("Fetching flight {} for trip {} for user: {}", flightId, tripId, email);
                Trip trip = tripRepository.findById(tripId)
                                .orElseThrow(() -> new TripNotFoundException(tripId));
                validateTripOwnership(trip, email);
                TrackedFlight flight = trackedFlightRepository.findById(flightId)
                                .orElseThrow(() -> new FlightNotFoundException(flightId));
                return mapToResponse(flight);
        }

        @Transactional
        public void deleteFlightFromTrip(String email, Long tripId, Long flightId) {
                log.info("Deleting flight {} from trip {} for user: {}", flightId, tripId, email);
                Trip trip = tripRepository.findById(tripId)
                                .orElseThrow(() -> new TripNotFoundException(tripId));
                validateTripOwnership(trip, email);
                TrackedFlight flight = trackedFlightRepository.findById(flightId)
                                .orElseThrow(() -> new FlightNotFoundException(flightId));
                trackedFlightRepository.delete(flight);
                log.info("Flight {} deleted successfully from trip {}", flightId, tripId);
        }

        private void validateTripOwnership(Trip trip, String email) {
                if (!trip.getUser().getEmail().equals(email)) {
                        throw new UnauthorizedAccessException(
                                        "Access denied — trip does not belong to user");
                }
        }

        private TrackedFlightResponse mapToResponse(TrackedFlight flight) {
                return new TrackedFlightResponse(
                                flight.getId(),
                                flight.getTrip().getId(),
                                flight.getFlightNumber(),
                                flight.getAirlineName(),
                                flight.getOriginIata(),
                                flight.getDestinationIata(),
                                flight.getSeatNumber(),
                                flight.getSeatType(),
                                flight.getCabinClass(),
                                flight.getReferenceNumber(),
                                flight.getScheduledDeparture(),
                                flight.getScheduledArrival(),
                                flight.getActualDeparture(),
                                flight.getActualArrival(),
                                flight.getDepartureDelayMinutes(),
                                flight.getArrivalDelayMinutes(),
                                flight.getStatus(),
                                flight.getLastSyncedAt());
        }
}