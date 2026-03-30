package com.demo.stratus.repository;

import com.demo.stratus.model.FlightStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FlightStatusHistoryRepository extends JpaRepository<FlightStatusHistory, Long> {
    List<FlightStatusHistory> findByTrackedFlightIdOrderByRecordedAtDesc(Long trackedFlightId);
}