package com.demo.stratus.model;

import com.demo.stratus.enums.CabinClass;
import com.demo.stratus.enums.FlightStatus;
import com.demo.stratus.enums.SeatType;
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

    @Column(name = "airline_name")
    private String airlineName;

    @Column(name = "origin_iata", length = 3)
    private String originIata;

    @Column(name = "destination_iata", length = 3)
    private String destinationIata;

    @Column(name = "seat_number")
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type")
    private SeatType seatType;

    @Enumerated(EnumType.STRING)
    @Column(name = "cabin_class")
    private CabinClass cabinClass;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "scheduled_departure")
    private LocalDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival")
    private LocalDateTime scheduledArrival;

    @Column(name = "actual_departure")
    private LocalDateTime actualDeparture;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "departure_delay_minutes")
    private Integer departureDelayMinutes;

    @Column(name = "arrival_delay_minutes")
    private Integer arrivalDelayMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @OneToMany(mappedBy = "trackedFlight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightStatusHistory> statusHistory;
}