package com.alexandrupanait.cpdlc_simulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexandrupanait.cpdlc_simulator.repository.AircraftRepository;


@Service
public class AircraftService {

@Autowired
private AircraftRepository aircraftRepository;

}