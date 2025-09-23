// AircraftController.java for handling HTTP requests related to Aircraft
package com.alexandrupanait.cpdlc_simulator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.service.AircraftService;
@RestController
@RequestMapping("/api/aircraft")
public class AircraftController {
    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    // Endpoints for managing aircraft 

    @GetMapping
    public List<Aircraft> getAllAircraft() {
        return aircraftService.getAllAircraft();
    }

    @PostMapping
    public Aircraft createAircraft(@RequestBody Aircraft aircraft) {
        return aircraftService.addAircraft(aircraft);
    }

    @PostMapping("/{callsign}/message")
    public Aircraft addMessageToAircraft(@PathVariable String callsign, @RequestBody String message) {
        return aircraftService.addMessageToAircraft(callsign, message);
    }
    

}