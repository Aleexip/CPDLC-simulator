package com.alexandrupanait.cpdlc_simulator.util;

import java.util.Map;

public class AirportCoordinates {
    public static Map<String, double[]> airportCoords = Map.ofEntries(
        Map.entry("KJFK", new double[]{40.6413, -73.7781}),
        Map.entry("EGLL", new double[]{51.4700, -0.4543}),
        Map.entry("LFPG", new double[]{49.0097, 2.5479}),
        Map.entry("EDDF", new double[]{50.0379, 8.5622}),
        Map.entry("EHAM", new double[]{52.3105, 4.7683}),
        Map.entry("KORD", new double[]{41.9742, -87.9073}),
        Map.entry("KLAX", new double[]{33.9416, -118.4085}),
        Map.entry("KSFO", new double[]{37.6213, -122.3790}),
        Map.entry("OMDB", new double[]{25.2532, 55.3657}),
        Map.entry("YSSY", new double[]{-33.9399, 151.1753})
    );
}
