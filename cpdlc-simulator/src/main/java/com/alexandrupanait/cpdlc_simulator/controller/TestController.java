package com.alexandrupanait.cpdlc_simulator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for testing the CPDLC application status.
 * Provides a simple endpoint to verify that the application is running.
 */
@RestController
public class TestController {

    
   
    @GetMapping("/")
    public String home() {
        return "CPDLC Simulator is running!";
    }
}
