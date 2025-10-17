package com.alexandrupanait.cpdlc_simulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.parser.FlightDataParser;
import com.alexandrupanait.cpdlc_simulator.repository.AirportRepository;
import com.alexandrupanait.cpdlc_simulator.service.AircraftService;

@SpringBootApplication
public class CpdlcSimulatorApplication implements CommandLineRunner {

    private final AircraftService aircraftService;
    private final FlightDataParser parser;
    private final AirportRepository airportRepository;

    // Constructor for DI
    public CpdlcSimulatorApplication(AircraftService aircraftService, 
                                     FlightDataParser parser,
                                     AirportRepository airportRepository) {
        this.aircraftService = aircraftService;
        this.parser = parser;
        this.airportRepository = airportRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CpdlcSimulatorApplication.class, args);
    }

    @Override
    @Order(2) // Run AFTER AirportDataLoader
    public void run(String... args) throws Exception {
        System.out.println("=== Starting Aircraft Data Load ===");
        
        // Verify airports are loaded
        long airportCount = airportRepository.count();
        System.out.println("Airports available in database: " + airportCount);
        
        if (airportCount == 0) {
            System.err.println("WARNING: No airports loaded! Aircraft may have invalid coordinates.");
        }

        // Load flight plan files from classpath
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:flights_data/*.fp");

        if (resources.length == 0) {
            System.out.println("No flight plan files found in the specified directory.");
            return;
        }

        System.out.println("Found " + resources.length + " flight plan files");
        List<Aircraft> aircraftList = new ArrayList<>();

        for (Resource resource : resources) {
            List<String> lines;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                lines = reader.lines().toList();
            }

            Aircraft aircraft = parser.parseAircraftDataLines(lines);
            
            if (aircraft == null) {
                System.out.println("Failed to parse: " + resource.getFilename());
                continue;
            }
            
            if (!aircraftService.existsByCallsign(aircraft.getCallsign())) {
                aircraftService.addAircraft(aircraft);
                aircraftList.add(aircraft);
                System.out.println("Saved aircraft: " + aircraft.getCallsign());
            } else {
                System.out.println("Duplicate aircraft skipped: " + aircraft.getCallsign());
            }
        }

        /* 
        // Print aircraft data
        System.out.println("\n=== Aircraft Summary ===");
        for (Aircraft ac : aircraftList) {
            System.out.println("=== Aircraft ===");
            System.out.println("Callsign: " + ac.getCallsign());
            System.out.println("Departure: " + ac.getDepartureAirport());
            System.out.println("Arrival: " + ac.getArrivalAirport());
            System.out.println("Position: " + ac.getLatitude() + ", " + ac.getLongitude());
            System.out.println("FL: " + ac.getFlightLevel());
            System.out.println("Aircraft Type: " + ac.getAircraftType());
            System.out.println("Speed: " + ac.getSpeed());
            System.out.println("Heading: " + ac.getHeading());
            System.out.println("Airline: " + ac.getAirline());
            System.out.println();
        }
            */
    }
}