package com.alexandrupanait.cpdlc_simulator.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cpdlc_message")
public class CpdlcMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   //the relation bewteen CpdlcMessage and CpdlcSession
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private CpdlcSession session;

    private String sender;       // who sent the message (e.g., "pilot" or "atc")
    private String messageType;  // type of the message 
    private String content;      // the actual message content
    private LocalDateTime sentAt;

    //Constructors 
    public CpdlcMessage() {}

    public CpdlcMessage(CpdlcSession session, String sender, String messageType, String content, LocalDateTime sentAt) {
        this.session = session;
        this.sender = sender;
        this.messageType = messageType;
        this.content = content;
        this.sentAt = sentAt;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public CpdlcSession getSession() {
        return session;
    }

    public void setSession(CpdlcSession session) {
        this.session = session;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
    
   public void setTimestamp(LocalDateTime timestamp) {
        this.sentAt = timestamp;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
