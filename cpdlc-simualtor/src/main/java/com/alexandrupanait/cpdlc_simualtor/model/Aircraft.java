package com.alexandrupanait.cpdlc_simualtor.model;

/**
 * Represents an aircraft in the CPDLC simulation.
 * Stores basic information such as callsign, position, and altitude.
 */
public class Aircraft {

    private String callsign;
    private double latitude;
    private double longitude;
    private double altitude;

    /**
     * Constructs an Aircraft object with the specified parameters.
     * @param callsign the aircraft's callsign
     * @param latitude the latitude position
     * @param longitude the longitude position
     * @param altitude the altitude in feet
     */
    public Aircraft(String callsign, double latitude, double longitude, double altitude){
        this.callsign = callsign;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Gets the aircraft's callsign.
     * @return the callsign
     */
    public String getCallsign() {
        return callsign;
    }

    /**
     * Gets the latitude of the aircraft.
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of the aircraft.
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets the altitude of the aircraft.
     * @return the altitude in feet
     */
    public double getAltitude() {
        return altitude;
    }
}
