package com.alexandrupanait.cpdlc_simulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource; // Import for resource handling
import org.springframework.core.io.support.PathMatchingResourcePatternResolver; // Import for resource loading

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.parser.FlightDataParser;
import com.alexandrupanait.cpdlc_simulator.service.AircraftService;

@SpringBootApplication
public class CpdlcSimulatorApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(CpdlcSimulatorApplication.class, args);

        AircraftService aircraftService = context.getBean(AircraftService.class);

		try{
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath:flights_data/*.fp");

			if (resources.length == 0) {
				System.out.println("No flight plan files found in the specified directory.");
				return;
			}

			FlightDataParser parser = new FlightDataParser();
			List<Aircraft> aircraftList = new ArrayList<>();

        for (Resource resource : resources) {
                List<String> lines;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    lines = reader.lines().toList();
                }

                Aircraft aircraft = parser.parseAircraftDataLines(lines);
                if (aircraft != null){ 
                    aircraftList.add(aircraft);
                    aircraftService.addAircraft(aircraft);
                    System.out.println("Saved aircraft: " + aircraft.getCallsign());
                }
            }
            
			/*  Print out the parsed aircraft data
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
                */
        } catch (Exception e) {
            e.printStackTrace();
    }
}

}