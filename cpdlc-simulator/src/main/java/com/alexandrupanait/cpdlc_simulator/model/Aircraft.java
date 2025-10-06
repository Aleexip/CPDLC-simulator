package com.alexandrupanait.cpdlc_simulator.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String callsign;
    private double latitude;
    private double longitude;
    private double altitude;
     private double heading;
    private double speed;
    private String departure_airport;
    private String arrival_airport;
    private String current_phase; // e.g., "climb", "cruise", "descent"
    private String aircraft_type;
    private int flight_level;
    private String airline;

    @ElementCollection
    private List<String> messageLog = new ArrayList<>();

    // Constructors
    
    public Aircraft() {
    }

 public Aircraft(String callsign, double latitude, double longitude, double altitude) {
    // set the 4 main fields
    // set reasonable defaults for the new fields
    this.callsign = callsign;
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;

    // default values
    this.heading = 0.0;
    this.speed = 250.0; // default speed in knots
    this.departure_airport = "UNKNOWN";
    this.arrival_airport = "UNKNOWN";
    this.current_phase = "cruise";
    this.aircraft_type = "A320"; // default aircraft type
    this.flight_level = (int)(altitude); 
    this.messageLog = new ArrayList<>();

}


    //Getters and Setters
    public String getCallsign() {
        return callsign;
    }
    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getAltitude() {
        return altitude;
    }
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getHeading() {
        return heading;
    }
    public void setHeading(double heading) {
        this.heading = heading;
    }
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public int getFlightLevel() {
        return flight_level;
    }
    public void setFlightLevel(int flight_level) {
        this.flight_level = flight_level;
    }

    public void setDepartureAirport(String departure_airport) {
        this.departure_airport = departure_airport;
    }
    public String getDepartureAirport() {
        return departure_airport;
    }
    public void setArrivalAirport(String arrival_airport) {
        this.arrival_airport = arrival_airport;
    }
    public String getArrivalAirport() {
        return arrival_airport;
    }

    public String getCurrentPhase() {
        return current_phase;
    }
    public String getAircraftType() {
        return aircraft_type;
    }
    public void setAircraftType(String aircraft_type) {
        this.aircraft_type = aircraft_type;
    }
   public List<String> getMessageLog() {
    return new ArrayList<>(this.messageLog);
}
    public void addMessageToLog(String message) {
        this.messageLog.add(message);
    }
    public Long getId() {
        return id;
    }
    public String getAirline() {
        return airline;
    }
    public void setAirline(String airline) {
        this.airline = airline;
    }

}