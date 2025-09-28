package com.alexandrupanait.cpdlc_simulator.parser;

import java.util.List;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;

public class FlightDataParser {
    // Method to parse flight data from a file and return an Aircraft object
    public Aircraft parseAircraftDataLines(List <String> lines) {
      
        if (lines == null || lines.isEmpty()) {
            return null; 
        }

        // Data extraction
        String callsign = extractCallsign(lines);
        String departureAirport = extractDepartureAirport(lines);
        String arrivalAirport = extractArrivalAirport(lines);
        double altitude= extractInitialAltitude(lines);
        
        //Create Aircraft object 

        Aircraft aircraft = new Aircraft(callsign, 0.0, 0.0, altitude);
        aircraft.setDepartureAirport(departureAirport);
        aircraft.setArrivalAirport(arrivalAirport);
        
        return aircraft;

      
    }

    private String extractCallsign(List <String> lines) {
        
        for (String line : lines) {
            if (line.startsWith("ACID")) {
                return line.split(":")[1].trim();
            }
        }

        return "UNKNOWN";
    }

    private String extractDepartureAirport(List <String> lines) {
        
        for (String line : lines) {
            if (line.startsWith("ADEP")) {
                return line.split(":")[1].trim();
            }
        }

        return "UNKNOWN";
    }

    private String extractArrivalAirport(List <String> lines) {
        for (String line : lines) {
            if (line.startsWith("ADES")) {
                return line.split(":")[1].trim();
            }
        }

        return "UNKNOWN";
    }
    private double extractInitialAltitude(List <String> lines) {
       for (String line : lines) {
        if(line.contains("RFL_VALUE")) {
            String[] parts = line.split("=");
            return Double.parseDouble(parts[1].trim());
        }
       }
       return 0;
    }
}
