package com.alexandrupanait.cpdlc_simulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alexandrupanait.cpdlc_simulator.parser.FlightDataParser;
import com.alexandrupanait.cpdlc_simulator.repository.AirportRepository;

@Configuration
public class ParserConfig {

    private final AirportRepository airportRepository;

    public ParserConfig(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Bean
    public FlightDataParser flightDataParser() {
        return new FlightDataParser(airportRepository);
    }
}