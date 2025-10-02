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
        double flightLevel= extractInitialFL(lines);
        String aircraftType= extractAircraftType(lines);
        double speed = extractSpeed(lines);
        String airline = callsign.substring(0, 3);
        //Create Aircraft object 

        Aircraft aircraft = new Aircraft(callsign, 0.0, 0.0, flightLevel);
        aircraft.setDepartureAirport(departureAirport);
        aircraft.setArrivalAirport(arrivalAirport);
        aircraft.setAircraftType(aircraftType);
        aircraft.setFlightLevel((int)flightLevel);
        aircraft.setSpeed(speed);
        aircraft.setAirline(airline);
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
    private double extractInitialFL(List <String> lines) {
    for (String line : lines) {
        if(line.contains("RFL_VALUE")) {
            String[] parts = line.split("=");
            if (parts.length > 1) {
                String value = parts[1].trim();
                try {
                    return Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: invalid flight level: " + value);
                    return 0;
                }
            }
        }
    }
    return 0;
}

    private String extractAircraftType(List <String> lines){
        for(String line:lines) {
            if(line.startsWith("Aircraft Type")) {
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
        if (line.startsWith("AcType") || line.startsWith("======")) {
            continue;
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length >= 5) {
            String speedStr = parts[4]; 
            if (speedStr.startsWith("K")) {
                String numericPart = speedStr.substring(1);
                try {
                    return Double.parseDouble(numericPart);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: invalid speed: " + speedStr);
                    return 0;
                }
            }
        }
    }
    return 0;
}

    

}
