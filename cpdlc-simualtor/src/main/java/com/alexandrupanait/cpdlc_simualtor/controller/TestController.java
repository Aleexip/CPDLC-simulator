package com.alexandrupanait.cpdlc_simualtor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for testing the CPDLC application status.
 * Provides a simple endpoint to verify that the application is running.
 */
@RestController
public class TestController {

    /**
     * Home endpoint to confirm the application is working.
     * @return a status message in Romanian
     */
    @GetMapping("/")
    public String home() {
        return "Aplicația CPDLC funcționează!";
    }
}
