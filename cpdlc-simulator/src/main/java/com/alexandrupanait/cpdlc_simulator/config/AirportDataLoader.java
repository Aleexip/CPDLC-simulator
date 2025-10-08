package com.alexandrupanait.cpdlc_simulator.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.alexandrupanait.cpdlc_simulator.model.Airport;
import com.alexandrupanait.cpdlc_simulator.repository.AirportRepository;

@Component
public class AirportDataLoader implements CommandLineRunner {

    private final AirportRepository airportRepository;

    public AirportDataLoader(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
public void run(String... args) throws Exception {
    ClassPathResource resource = new ClassPathResource("airports.csv");

    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

        String line;
        boolean firstLine = true;

        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }

            // Split by comma, handling commas inside quoted fields
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            if (parts.length <= 11) continue;

            // Clean each field using the helper method
            String icao = cleanField(parts[11].trim());
            String rawName = cleanField(parts[1].trim());

            if (icao.isEmpty()) continue;

            // Use a default name if cleaned name is empty
            final String name = rawName.isEmpty() ? "Unknown" : rawName;

            double lat, lng;
            try {
                lat = Double.parseDouble(cleanField(parts[6]));
                lng = Double.parseDouble(cleanField(parts[7]));
            } catch (NumberFormatException e) {
                continue;
            }

            airportRepository.findByIcao(icao).orElseGet(() -> {
                Airport airport = new Airport(icao, name, lat, lng);
                return airportRepository.save(airport);
            });
        }
    }
    System.out.println("Airport CSV loaded successfully.");
}

// New helper method to remove surrounding quotes and trim whitespace
private String cleanField(String field) {
    if (field == null) {
        return "";
    }
    String cleaned = field.trim();
    // Remove leading and trailing quotes if present
    if (cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
        cleaned = cleaned.substring(1, cleaned.length() - 1);
    }
    return cleaned.trim();
}
}