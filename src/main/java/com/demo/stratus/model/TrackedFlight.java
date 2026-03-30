package com.demo.stratus.model;

import com.demo.stratus.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tracked_flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackedFlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "origin_iata", nullable = false, length = 3)
    private String originIata;

    @Column(name = "destination_iata", nullable = false, length = 3)
    private String destinationIata;

    @Column(name = "scheduled_departure")
    private LocalDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival")
    private LocalDateTime scheduledArrival;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @OneToMany(mappedBy = "trackedFlight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightStatusHistory> statusHistory;
}