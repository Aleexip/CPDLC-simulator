package com.alexandrupanait.cpdlc_simulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.parser.FlightDataParser;
import com.alexandrupanait.cpdlc_simulator.service.AircraftService;

@SpringBootApplication
public class CpdlcSimulatorApplication implements CommandLineRunner {

    private final AircraftService aircraftService;
    private final FlightDataParser parser;

    // Constructor for DI
    public CpdlcSimulatorApplication(AircraftService aircraftService, FlightDataParser parser) {
        this.aircraftService = aircraftService;
        this.parser = parser;
    }

    public static void main(String[] args) {
        SpringApplication.run(CpdlcSimulatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear existing aircraft data dacă vrei
        // aircraftService.deleteAllAircraft();

        // Load flight plan files from classpath
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:flights_data/*.fp");

        if (resources.length == 0) {
            System.out.println("No flight plan files found in the specified directory.");
            return;
        }

        List<Aircraft> aircraftList = new ArrayList<>();

        for (Resource resource : resources) {
            List<String> lines;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                lines = reader.lines().toList();
            }

            Aircraft aircraft = parser.parseAircraftDataLines(lines);
            
            if (!aircraftService.existsByCallsign(aircraft.getCallsign())) {
                aircraftService.addAircraft(aircraft);
                aircraftList.add(aircraft);
                System.out.println("Saved aircraft: " + aircraft.getCallsign());
            } else {
                System.out.println("Duplicate aircraft skipped: " + aircraft.getCallsign());
            }
        }

        // Print aircraft data
        for (Aircraft ac : aircraftList) {
            System.out.println("=== Aircraft ===");
            System.out.println("Callsign: " + ac.getCallsign());
            System.out.println("Departure: " + ac.getDepartureAirport());
            System.out.println("Arrival: " + ac.getArrivalAirport());
            System.out.println("FL: " + ac.getFlightLevel());
            System.out.println("Aircraft Type: " + ac.getAircraftType());
            System.out.println("Speed: " + ac.getSpeed());
            System.out.println("Airline: " + ac.getAirline());
        }
    }
}