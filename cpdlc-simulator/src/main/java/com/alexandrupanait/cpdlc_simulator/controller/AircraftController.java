// AircraftController.java for handling HTTP requests related to Aircraft
package com.alexandrupanait.cpdlc_simulator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.service.AircraftService;

@CrossOrigin(origins = "http://localhost:3000") // Allow requests from React frontend
@RestController
@RequestMapping("/api/aircraft") // Base path for all aircraft-related endpoints
public class AircraftController {
    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    // Endpoints for managing aircraft 

    // Endpoint to get all aircraft
    @GetMapping
    public List<Aircraft> getAllAircraft() {
        return aircraftService.getAllAircraft();
    }

    

    // endpoint to add a message to an aircraft's log
    @PostMapping("/{callsign}/message")
    public Aircraft addMessageToAircraft(@PathVariable String callsign, @RequestBody String message) {
        return aircraftService.addMessageToAircraft(callsign, message);
    }
}
