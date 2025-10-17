package com.alexandrupanait.cpdlc_simulator.parser;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.model.Airport;
import com.alexandrupanait.cpdlc_simulator.repository.AirportRepository;

@Component
public class FlightDataParser {
    private final AirportRepository airportRepository;

    public FlightDataParser(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Aircraft parseAircraftDataLines(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return null; 
        }

        // Data extraction
        String callsign = extractCallsign(lines);
        String departureAirport = extractDepartureAirport(lines);
        String arrivalAirport = extractArrivalAirport(lines);
        double flightLevel = extractInitialFL(lines);
        String aircraftType = extractAircraftType(lines);
        double speed = extractSpeed(lines);
        String airline = callsign.substring(0, 3);

        // Get coordinates for ARRIVAL airport (spawn at destination)
        double[] arrCoords = getAirportCoordinates(arrivalAirport);
        double[] depCoords = getAirportCoordinates(departureAirport);

        // Calculate heading from departure to arrival
        double heading = Math.toDegrees(Math.atan2(
            arrCoords[1] - depCoords[1],
            arrCoords[0] - depCoords[0]
        ));

        // Create aircraft at ARRIVAL airport coordinates
        Aircraft aircraft = new Aircraft(callsign, depCoords[0], depCoords[1], flightLevel);
        aircraft.setHeading((heading + 360) % 360);
        aircraft.setDepartureAirport(departureAirport);
        aircraft.setArrivalAirport(arrivalAirport);
        aircraft.setAircraftType(aircraftType);
        aircraft.setFlightLevel((int) flightLevel);
        aircraft.setSpeed(speed);
        aircraft.setAirline(airline);
        aircraft.setCurrentPhase("cruise");
        

        return aircraft;
    }

    private double[] getAirportCoordinates(String icao) {
        if (icao == null) return new double[]{0.0, 0.0};

        String cleanIcao = icao.replace("\"", "").trim();

        Optional<Airport> airportOpt = airportRepository.findByIcao(cleanIcao);
        if (airportOpt.isEmpty()) {
            System.out.println("Airport not found in DB: [" + cleanIcao + "]");
        }

        return airportOpt.map(a -> new double[]{a.getLatitude(), a.getLongitude()})
                         .orElse(new double[]{0.0, 0.0});
    }

    private String extractCallsign(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("ACID")) {
                return line.split(":")[1].trim();
            }
        }
        return "UNKNOWN";
    }

    private String extractDepartureAirport(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("ADEP")) {
                return line.split(":")[1].trim();
            }
        }
        return "UNKNOWN";
    }

    private String extractArrivalAirport(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("ADES")) {
                return line.split(":")[1].trim();
            }
        }
        return "UNKNOWN";
    }

    private double extractInitialFL(List<String> lines) {
    for (String line : lines) {
        // look for RFL line
        if (line.trim().startsWith("RFL")) {
            // extract flight level
            String[] parts = line.split(":");
            if (parts.length > 1) {
                String value = parts[1].trim(); // ex: "F380"
                if (value.startsWith("F")) {
                    try {
                        return Double.parseDouble(value.substring(1));
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: invalid flight level: " + value);
                        return 0;
                    }
                }
            }
        }
    }
    return 0;
}

    private String extractAircraftType(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("Aircraft Type")) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    return parts[1].trim();
                }
            }
        }
        return "UNKNOWN";
    }

private double extractSpeed(List<String> lines) {
    for (String line : lines) {
        line = line.trim();
        if (line.startsWith("TAS") || line.startsWith("Speed")) {
          // extract speed
            String[] parts = line.split(":");
            if (parts.length >= 2) {
                String speedStr = parts[1].trim();
                
                if (speedStr.startsWith("N") || speedStr.startsWith("K")) {
                    try {
                        return Double.parseDouble(speedStr.substring(1));
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: invalid speed: " + speedStr);
                        return 0;
                    }
                }
            }
        }
    }
    return 0;
}

}

   