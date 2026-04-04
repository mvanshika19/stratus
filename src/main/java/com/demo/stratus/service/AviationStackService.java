package com.demo.stratus.service;

import com.demo.stratus.dto.response.FlightDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class AviationStackService {

    @Value("${aviationstack.api.key}")
    private String apiKey;

    @Value("${aviationstack.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public FlightDataResponse getFlightData(String flightNumber) {
        try {
            log.info("Fetching flight data from AviationStack for: {}", flightNumber);

            String url = apiUrl + "/flights?access_key=" + apiKey
                    + "&flight_iata=" + flightNumber.toUpperCase();

            Map response = restTemplate.getForObject(url, Map.class);
            log.debug("Raw flight data from AviationStack: {}", response);
            if (response != null && response.containsKey("data")) {
                java.util.List<Map> data = (java.util.List<Map>) response.get("data");

                if (data != null && !data.isEmpty()) {
                    Map flight = data.get(0);
                    log.info("Flight data found for: {}", flightNumber);
                    return extractFlightData(flight);
                }
            }

            log.warn("No flight data found in AviationStack for: {}", flightNumber);
            return null;

        } catch (Exception e) {
            log.error("Failed to fetch flight data from AviationStack for: {} | Error: {}",
                    flightNumber, e.getMessage());
            return null;
        }
    }

    private FlightDataResponse extractFlightData(Map flight) {
        FlightDataResponse data = new FlightDataResponse();

        // Airline
        Map airline = (Map) flight.get("airline");
        if (airline != null) {
            data.setAirlineName(getString(airline, "name"));
        }

        // Departure
        Map departure = (Map) flight.get("departure");
        if (departure != null) {
            data.setOriginIata(getString(departure, "iata"));
            data.setScheduledDeparture(getString(departure, "scheduled"));
            data.setActualDeparture(getString(departure, "actual"));
            data.setDepartureDelay(getInteger(departure, "delay"));
        }

        // Arrival
        Map arrival = (Map) flight.get("arrival");
        if (arrival != null) {
            data.setDestinationIata(getString(arrival, "iata"));
            data.setScheduledArrival(getString(arrival, "scheduled"));
            data.setActualArrival(getString(arrival, "actual"));
            data.setArrivalDelay(getInteger(arrival, "delay"));
        }

        // Status
        data.setStatus(getString(flight, "flight_status"));

        return data;
    }

    private String getString(Map map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private Integer getInteger(Map map, String key) {
        Object value = map.get(key);
        if (value == null)
            return null;
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}