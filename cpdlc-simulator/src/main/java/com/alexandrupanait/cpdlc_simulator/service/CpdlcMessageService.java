package com.alexandrupanait.cpdlc_simulator.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alexandrupanait.cpdlc_simulator.model.CpdlcMessage;
import com.alexandrupanait.cpdlc_simulator.model.CpdlcSession;
import com.alexandrupanait.cpdlc_simulator.repository.CpdlcMessageRepository;
import com.alexandrupanait.cpdlc_simulator.repository.CpdlcSessionRepository;

@Service

public class CpdlcMessageService {
    
    private final CpdlcSessionRepository cpdlcSessionRepository;
    private final CpdlcMessageRepository cpdlcMessageRepository;
    
    public CpdlcMessageService(CpdlcSessionRepository cpdlcSessionRepository, CpdlcMessageRepository cpdlcMessageRepository) {
    this.cpdlcSessionRepository = cpdlcSessionRepository;
    this.cpdlcMessageRepository = cpdlcMessageRepository;

}

public void addMessage(Long sessionId, String sender, String messageType, String content){
    Optional<CpdlcSession> optionalSession = cpdlcSessionRepository.findById(sessionId);
    if(optionalSession.isPresent()){
        CpdlcSession session = optionalSession.get();

        CpdlcMessage message = new CpdlcMessage();
        message.setSession(session);
        message.setSender(sender);
        message.setMessageType(messageType);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());

        cpdlcMessageRepository.save(message);
    } else {
        System.out.println("Session with id " + sessionId + " not found!");
    }
    }

    public List<CpdlcMessage> getMessagesBySession(Long sessionId) {
        return cpdlcMessageRepository.findBySessionId(sessionId);
    }
}