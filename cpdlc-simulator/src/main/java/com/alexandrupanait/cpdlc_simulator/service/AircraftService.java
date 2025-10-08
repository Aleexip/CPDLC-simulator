// AircraftService.java for business logic related to Aircraft
package com.alexandrupanait.cpdlc_simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alexandrupanait.cpdlc_simulator.model.Aircraft;
import com.alexandrupanait.cpdlc_simulator.parser.FlightDataParser;
import com.alexandrupanait.cpdlc_simulator.repository.AircraftRepository;


@Service
public class AircraftService {

    private final FlightDataParser parser;
    private final AircraftRepository aircraftRepository;

    // Use constructor injection for both dependencies
    public AircraftService(FlightDataParser parser, AircraftRepository aircraftRepository) {
        this.parser = parser;
        this.aircraftRepository = aircraftRepository;
    }

    // Remove the @Autowired annotation and use the repository directly
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

    public Aircraft parseAircraft(List<String> lines) {
        return parser.parseAircraftDataLines(lines);
    }
}