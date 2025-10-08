package com.alexandrupanait.cpdlc_simulator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Airport {

    @Id
    private String icao;
    private String name;
    private double latitude;
    private double longitude;

    public Airport() {}

    
    public Airport(String icao, String name, double latitude, double longitude) {
        this.icao = icao;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters și Setters
    public String getIcao() { return icao; }
    public void setIcao(String icao) { this.icao = icao; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
