// DataLoader.java for initializing sample data
package com.alexandrupanait.cpdlc_simulator.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.alexandrupanait.cpdlc_simulator.repository.AircraftRepository;

@Component
public class DataLoader implements CommandLineRunner{
   private final AircraftRepository aircraftRepository;

    public DataLoader(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

   @Override
    public void run(String... args) throws Exception {
       
    }
}