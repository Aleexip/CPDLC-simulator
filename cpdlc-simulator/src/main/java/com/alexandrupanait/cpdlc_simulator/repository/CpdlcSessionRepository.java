package com.alexandrupanait.cpdlc_simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexandrupanait.cpdlc_simulator.model.CpdlcSession;

public interface CpdlcSessionRepository  extends JpaRepository<CpdlcSession, Long> {
    
}
