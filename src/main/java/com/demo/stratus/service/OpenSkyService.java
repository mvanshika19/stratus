package com.demo.stratus.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OpenSkyService {

    private static final String OPENSKY_BASE_URL = "https://opensky-network.org/api";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getFlightData(String flightNumber) {
        try {
            log.info("Fetching flight data from OpenSky for: {}", flightNumber);

            String callsign = flightNumber.toUpperCase().replace(" ", "");
            String url = OPENSKY_BASE_URL + "/states/all";

            Map response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("states")) {
                java.util.List<java.util.List<Object>> states =
                        (java.util.List<java.util.List<Object>>) response.get("states");

                if (states != null) {
                    for (java.util.List<Object> state : states) {
                        if (state != null && state.size() > 1) {
                            String stateCallsign = state.get(1) != null
                                    ? state.get(1).toString().trim()
                                    : "";
                            if (stateCallsign.equalsIgnoreCase(callsign)) {
                                log.info("Flight found in OpenSky: {}", flightNumber);
                                return extractFlightData(state);
                            }
                        }
                    }
                }
            }

            log.warn("Flight not found in OpenSky: {}", flightNumber);
            return null;

        } catch (Exception e) {
            log.error("Failed to fetch flight data from OpenSky for: {} | Error: {}",
                    flightNumber, e.getMessage());
            return null;
        }
    }

    private Map<String, Object> extractFlightData(java.util.List<Object> state) {
        Map<String, Object> flightData = new HashMap<>();

        // OpenSky state vector fields:
        // 0: icao24, 1: callsign, 2: origin_country, 3: time_position
        // 4: last_contact, 5: longitude, 6: latitude, 7: baro_altitude
        // 8: on_ground, 9: velocity, 10: true_track, 11: vertical_rate
        // 12: sensors, 13: geo_altitude, 14: squawk, 15: spi, 16: position_source

        flightData.put("callsign", state.get(1) != null ? state.get(1).toString().trim() : "");
        flightData.put("originCountry", state.get(2) != null ? state.get(2).toString() : "");
        flightData.put("onGround", state.get(8) != null && (Boolean) state.get(8));
        flightData.put("velocity", state.get(9));
        flightData.put("latitude", state.get(6));
        flightData.put("longitude", state.get(5));

        return flightData;
    }
}