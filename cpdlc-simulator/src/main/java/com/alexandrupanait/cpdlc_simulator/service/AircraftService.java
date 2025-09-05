package com.alexandrupanait.cpdlc_simulator.service;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing aircraft data.
 * Provides methods to retrieve aircraft for the CPDLC simulation.
 */
@Service
public class AircraftService {

    /**
     * Retrieves a list of all aircraft in the simulation.
     * @return a list of Aircraft objects
     */
    public List<Aircraft> getAllAircraft() {
        List <Aircraft> aircraftList = new ArrayList<>();

        // Add sample aircraft data for simulation
        aircraftList.add(new Aircraft("TAROM123", 44.43, 26.10, 10000));
        aircraftList.add(new Aircraft("RYANAIR456", 45.00, 25.00, 12000));
        aircraftList.add(new Aircraft("LUFTHANSA789", 46.50, 24.50, 11000));

        return aircraftList;
    }
}
