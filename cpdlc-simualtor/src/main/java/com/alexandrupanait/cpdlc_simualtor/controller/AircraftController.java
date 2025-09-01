package com.alexandrupanait.cpdlc_simualtor.controller;

import com.alexandrupanait.cpdlc_simualtor.model.Aircraft;
import com.alexandrupanait.cpdlc_simualtor.service.AircraftService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for handling aircraft-related endpoints.
 * This controller provides endpoints to retrieve aircraft data for the CPDLC simulation app.
 */
@RestController
public class AircraftController {

    private final AircraftService aircraftService;

    /**
     * Constructor for AircraftController.
     * @param aircraftService the service used to manage aircraft data
     */
    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    /**
     * Retrieves a list of all aircraft in the system.
     * @return a list of Aircraft objects
     */
    @GetMapping("/api/aircraft")
    public List<Aircraft> getAircraft() {
        // Delegate to the service layer to fetch all aircraft
        return aircraftService.getAllAircraft();
    }
}
