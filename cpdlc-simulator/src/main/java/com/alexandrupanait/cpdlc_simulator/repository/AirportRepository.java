package com.alexandrupanait.cpdlc_simulator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexandrupanait.cpdlc_simulator.model.Airport;


public interface AirportRepository extends JpaRepository<Airport, String> {
    Optional<Airport> findByIcao(String icao);
}
