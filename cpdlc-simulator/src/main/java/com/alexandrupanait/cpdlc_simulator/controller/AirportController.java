package com.alexandrupanait.cpdlc_simulator.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexandrupanait.cpdlc_simulator.model.Airport;
import com.alexandrupanait.cpdlc_simulator.repository.AirportRepository;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    @Autowired
    private AirportRepository airportRepository;

    // Get airport by ICAO
    @GetMapping("/{icao}")
    public Airport getAirport(@PathVariable String icao) {
        Optional<Airport> airport = airportRepository.findByIcao(icao);
        return airport.orElse(null); 
    }

  // Get all airports
    @GetMapping
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }
    
}
