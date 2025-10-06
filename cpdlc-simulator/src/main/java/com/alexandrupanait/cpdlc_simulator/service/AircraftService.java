// AircraftService.java for business logic related to Aircraft
package com.alexandrupanait.cpdlc_simulator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.repository.AircraftRepository;


@Service
public class AircraftService {


   public List<Aircraft> getAllAircraft() {
        return aircraftRepository.findAll();
    }
    
    public Aircraft addAircraft(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }


    public Aircraft addMessageToAircraft(String callsign, String message) {
    Aircraft aircraft = aircraftRepository.findByCallsign(callsign);
    if (aircraft != null) {
        aircraft.addMessageToLog(message);
        return aircraftRepository.save(aircraft);
    }

    return null; 
}

public void deleteAllAircraft() {
        aircraftRepository.deleteAll(); 
    }
public boolean existsByCallsign(String callsign) {
        return aircraftRepository.existsByCallsign(callsign);
    }

@Autowired 
private AircraftRepository aircraftRepository;


}