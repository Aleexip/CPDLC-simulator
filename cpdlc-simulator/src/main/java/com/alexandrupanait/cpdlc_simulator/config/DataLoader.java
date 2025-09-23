// DataLoader.java for initializing sample data
package com.alexandrupanait.cpdlc_simulator.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.repository.AircraftRepository;

@Component
public class DataLoader implements CommandLineRunner{
   private final AircraftRepository aircraftRepository;

    public DataLoader(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

   @Override
    public void run(String... args) throws Exception {
        // Create and save a sample aircraft
        if (aircraftRepository.count() == 0) {
        Aircraft a1 = new Aircraft("TEST123", 40.7128, -74.0060, 30000);
        Aircraft a2 = new Aircraft("FLY456", 51.5074, -0.1278, 28000);
        aircraftRepository.save(a1);
        aircraftRepository.save(a2);
        }
    }
}
