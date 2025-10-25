package com.alexandrupanait.cpdlc_simulator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexandrupanait.cpdlc_simulator.model.CpdlcMessage;
import com.alexandrupanait.cpdlc_simulator.service.CpdlcMessageService;
import com.alexandrupanait.cpdlc_simulator.service.CpdlcSessionService;




@RestController
@RequestMapping("/api/cpdlc")
public class CpdlcController {
    
    private final CpdlcSessionService sessionService;
    private final CpdlcMessageService messageService;

    public CpdlcController(CpdlcSessionService sessionService, CpdlcMessageService messageService) {
        this.sessionService = sessionService;
        this.messageService = messageService;
    }

    //Create a new CPDLC session
    @PostMapping("/session/{callsign}")
    public void createSession(@PathVariable String callsign){
        sessionService.createSession(callsign);
    }

    //Close an existing CPDLC session
    @PostMapping("/session/{id}/close")
    public void closeSession(@PathVariable Long id) {
        sessionService.closeSession(id);
    }
    
    //Add a message to a CPDLC session
    @PostMapping("/session/{id}/message")
    public void sendMessage(
            @PathVariable Long id,
            @RequestParam String sender,
            @RequestParam String type,
            @RequestParam String content) {
        messageService.addMessage(id, sender, type, content);
    }

    //Post a message to a CPDLC session
    @GetMapping("/session/{id}/messages")
    public List<CpdlcMessage> getMessages(@PathVariable Long id) {
        return messageService.getMessagesBySession(id);
    }
    
}

