package com.alexandrupanait.cpdlc_simulator.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alexandrupanait.cpdlc_simulator.model.CpdlcMessage;
import com.alexandrupanait.cpdlc_simulator.model.CpdlcSession;
import com.alexandrupanait.cpdlc_simulator.repository.CpdlcMessageRepository;
import com.alexandrupanait.cpdlc_simulator.repository.CpdlcSessionRepository;
@Service 
public class CpdlcSessionService {
    
    private final CpdlcSessionRepository cpdlcSessionRepository;
    private final CpdlcMessageRepository cpdlcMessageRepository;
    
    public CpdlcSessionService(CpdlcSessionRepository cpdlcSessionRepository, CpdlcMessageRepository cpdlcMessageRepository) {
    this.cpdlcSessionRepository = cpdlcSessionRepository;
    this.cpdlcMessageRepository = cpdlcMessageRepository;

}


   public void createSession(String callsign){
        
        CpdlcSession session = new CpdlcSession();
        session.setCallsign(callsign);
        session.setStatus("ACTIVE");
        cpdlcSessionRepository.save(session);
        session.setStartedAt(LocalDateTime.now());

    }
    public void closeSession(Long sessionId){
    //set status to closed and closed at timestamp
    
    Optional<CpdlcSession> optionalSession = cpdlcSessionRepository.findById(sessionId);
    if(optionalSession.isPresent()){
        CpdlcSession session = optionalSession.get();
        session.setStatus("CLOSED");
        session.setClosedAt(LocalDateTime.now());
        cpdlcSessionRepository.save(session);

        }
    }

  public Optional<CpdlcSession> getSessionById(Long sessionId){
    return cpdlcSessionRepository.findById(sessionId);
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
        message.setTimestamp(LocalDateTime.now());

        cpdlcMessageRepository.save(message);
    } else {
    
        System.out.println("Session with id " + sessionId + " not found!");
    }
}



   
}
