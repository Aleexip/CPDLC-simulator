package com.alexandrupanait.cpdlc_simulator.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alexandrupanait.cpdlc_simulator.model.Airport;
import com.alexandrupanait.cpdlc_simulator.repository.AirportRepository;

@Component
@Order(1)
public class AirportDataLoader implements CommandLineRunner {

    private final AirportRepository airportRepository;

    public AirportDataLoader(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
        System.out.println("!!! AirportDataLoader CONSTRUCTED !!!");
    }

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("\n========================================");
        System.out.println("=== AIRPORT DATA LOADER STARTING ===");
        System.out.println("========================================\n");
        
        try {
            // Check if airports already loaded
            long existingCount = airportRepository.count();
            System.out.println("Current airports in DB: " + existingCount);
            
            if (existingCount > 0) {
                System.out.println("Airports already loaded. Skipping.");
                return;
            }

            ClassPathResource resource = new ClassPathResource("airports.csv");
            
            System.out.println("Looking for: airports.csv");
            System.out.println("Resource exists: " + resource.exists());
            
            if (!resource.exists()) {
                System.err.println("\n!!! ERROR: airports.csv NOT FOUND !!!");
                return;
            }

            int successCount = 0;
            int skipCount = 0;
            int lineNumber = 0;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                boolean firstLine = true;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    
                    if (firstLine) {
                        System.out.println("\nCSV Header found:");
                        System.out.println(line);
                        System.out.println("\nProcessing data rows...\n");
                        firstLine = false;
                        continue;
                    }

                    try {
                        String[] parts = parseCSVLine(line);
                        
                        if (parts.length < 13) {
                            skipCount++;
                            continue;
                        }

                        String name = cleanField(parts[3]);  // Column D: name
                        String icao = cleanField(parts[12]); // Column M: icao_code
                        
                        // Skip invalid ICAO codes
                        if (icao.isEmpty() || icao.equals("\\N") || icao.equals("no") || 
                            icao.equals("0") || icao.length() != 4) {
                            skipCount++;
                            continue;
                        }

                        // Parse coordinates
                        String latStr = cleanField(parts[4]);  // Column E: latitude_deg
                        String lngStr = cleanField(parts[5]);  // Column F: longitude_deg
                        
                        if (latStr.isEmpty() || lngStr.isEmpty() || 
                            latStr.equals("\\N") || lngStr.equals("\\N") ||
                            latStr.equals("no") || lngStr.equals("no")) {
                            skipCount++;
                            continue;
                        }
                        
                        double lat, lng;
                        try {
                            lat = Double.parseDouble(latStr);
                            lng = Double.parseDouble(lngStr);
                        } catch (NumberFormatException e) {
                            skipCount++;
                            continue;
                        }
                        
                        // Validate coordinate ranges
                        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
                            skipCount++;
                            continue;
                        }

                        String finalName = name.isEmpty() ? "Unknown Airport" : name;

                        // Save to database
                        Airport airport = new Airport(icao, finalName, lat, lng);
                        airportRepository.save(airport);
                        successCount++;
                        
                        // Show first 5 successful saves
                        if (successCount <= 5) {
                            System.out.println("✓ Saved: " + icao + " - " + finalName + " (" + lat + ", " + lng + ")");
                        }
                        
                        // Progress indicator
                        if (successCount % 1000 == 0) {
                            System.out.println("... " + successCount + " airports loaded so far ...");
                        }

                    } catch (Exception e) {
                        System.err.println("Error on line " + lineNumber + ": " + e.getMessage());
                        skipCount++;
                    }
                }
            }

            // Flush to ensure all saves are committed
            airportRepository.flush();
            
            long finalCount = airportRepository.count();
            
            System.out.println("\n========================================");
            System.out.println("AIRPORT LOAD COMPLETE ");
            System.out.println("Successfully parsed: " + successCount);
            System.out.println("Skipped entries: " + skipCount);
            System.out.println("Total lines: " + lineNumber);
            System.out.println("Airports in DB: " + finalCount);
            System.out.println("========================================\n");
            
            if (finalCount == 0) {
                System.err.println("WARNING: No airports in database !!!");
            }

        } catch (Exception e) {
            System.err.println("\n ERROR IN AIRPORT LOADER !!!");
            e.printStackTrace();
        }
    }

    private String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private String cleanField(String field) {
        if (field == null) return "";
        String cleaned = field.trim();
        if (cleaned.startsWith("\"") && cleaned.endsWith("\"") && cleaned.length() >= 2) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned.trim();
    }
}