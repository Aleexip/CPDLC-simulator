package com.alexandrupanait.cpdlc_simulator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexandrupanait.cpdlc_simulator.model.CpdlcMessage;

public interface CpdlcMessageRepository extends JpaRepository<CpdlcMessage, Long> {
    List <CpdlcMessageRepository> findBySessionId(Long sessionId);
}
