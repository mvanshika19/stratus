package com.demo.stratus.service;

import com.demo.stratus.dto.request.TripRequest;
import com.demo.stratus.dto.response.TripResponse;
import com.demo.stratus.exception.TripNotFoundException;
import com.demo.stratus.exception.UnauthorizedAccessException;
import com.demo.stratus.exception.UserNotFoundException;
import com.demo.stratus.model.Trip;
import com.demo.stratus.model.User;
import com.demo.stratus.repository.TripRepository;
import com.demo.stratus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Transactional
    public TripResponse createTrip(String email, TripRequest request) {
        log.info("Creating trip for user: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        Trip trip = new Trip();
        trip.setUser(user);
        trip.setName(request.getName());
        trip.setPurpose(request.getPurpose());
        trip.setAccompaniedMembers(request.getAccompaniedMembers());
        tripRepository.save(trip);
        log.info("Trip created successfully for user: {}", email);
        return mapToTripResponse(trip);
    }

    @Transactional
    public List<TripResponse> getAllTrips(String email) {
        log.info("Fetching all trips for user: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return tripRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToTripResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripResponse getTripById(String email, Long tripId) {
        log.info("Fetching trip {} for user: {}", tripId, email);
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));
        validateTripOwnership(trip, email);
        return mapToTripResponse(trip);
    }

    @Transactional
    public TripResponse updateTrip(String email, Long tripId, TripRequest request) {
        log.info("Updating trip {} for user: {}", tripId, email);
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));
        validateTripOwnership(trip, email);
        trip.setName(request.getName());
        trip.setPurpose(request.getPurpose());
        trip.setAccompaniedMembers(request.getAccompaniedMembers());
        tripRepository.save(trip);
        log.info("Trip {} updated successfully", tripId);
        return mapToTripResponse(trip);
    }

    @Transactional
    public void deleteTrip(String email, Long tripId) {
        log.info("Deleting trip {} for user: {}", tripId, email);
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));
        validateTripOwnership(trip, email);
        tripRepository.delete(trip);
        log.info("Trip {} deleted successfully", tripId);
    }

    private void validateTripOwnership(Trip trip, String email) {
        if (!trip.getUser().getEmail().equals(email)) {
            throw new UnauthorizedAccessException(
                    "Access denied — trip does not belong to user");
        }
    }

    private TripResponse mapToTripResponse(Trip trip) {
        return new TripResponse(
                trip.getId(),
                trip.getName(),
                trip.getPurpose(),
                trip.getAccompaniedMembers(),
                trip.getUser().getId(),
                trip.getCreatedAt()
        );
    }
}