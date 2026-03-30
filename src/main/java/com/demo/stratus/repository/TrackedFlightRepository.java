package com.demo.stratus.repository;

import com.demo.stratus.model.TrackedFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrackedFlightRepository extends JpaRepository<TrackedFlight, Long> {
    List<TrackedFlight> findByTripId(Long tripId);
    boolean existsByTripIdAndFlightNumber(Long tripId, String flightNumber);
}