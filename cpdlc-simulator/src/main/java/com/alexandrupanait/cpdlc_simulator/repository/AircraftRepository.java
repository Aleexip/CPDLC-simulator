package com.alexandrupanait.cpdlc_simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Aircraft findByCallsign(String callsign);
}