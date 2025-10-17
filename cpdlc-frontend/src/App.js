import React, { useEffect, useState, useRef } from "react";
import "leaflet/dist/leaflet.css";

import WeatherLayer from "./components/Weather/WeatherLayer";
import WeatherControls from "./components/Weather/WeatherControls";
import PlaneMarker from "./components/Aircraft/AircraftMarker";
import AirspaceMap from "./components/Map/MapContainer";
import PilotATCPanel from "./components/Controller/PilotAtcPanel";
import CommandPanel from "./components/Controller/CommandPanel";

import { aircraftService } from './services/aircraftService';

function App() {
  const [showClouds, setShowClouds] = useState(false);
  const [showWind, setShowWind] = useState(false);
  const [showPressure, setShowPressure] = useState(false);

  const [role, setRole] = useState("controller");
  const [selectedCallsign, setSelectedCallsign] = useState(null);
  
  const [planes, setPlanes] = useState([]);
  const planesRef = useRef(planes);

  // Fetch aircraft data from backend on component mount
  useEffect(() => {
    const fetchAircraft = async () => {
      try {
        const data = await aircraftService.fetchAircraft();
        console.log('Raw data from API:', data);
        
        // Transform and validate all planes at once
        const validPlanes = data
          .filter(plane => plane && plane.callsign && plane.latitude != null && plane.longitude != null)
          .map((plane, index) => ({
            ...plane,
            lat: plane.latitude,
            lng: plane.longitude,
            heading: Math.round((plane.heading || 0) * 10) / 10,
            speed: plane.speed || 0,
            flight_level: plane.flightLevel || 0,
            trail: [[plane.latitude, plane.longitude]],
            id: `${plane.callsign}_${Date.now()}_${index}` // Unique ID
          }));
        
        console.log('Valid planes to add:', validPlanes);
        
        // Add all planes at once instead of staggered
        setPlanes(prevPlanes => {
          // Get existing callsigns to avoid duplicates
          const existingCallsigns = new Set(
            prevPlanes.filter(p => p?.callsign).map(p => p.callsign)
          );
          
          // Filter out planes that already exist
          const newPlanes = validPlanes.filter(plane => 
            !existingCallsigns.has(plane.callsign)
          );
          
          console.log('New unique planes:', newPlanes);
          return [...prevPlanes, ...newPlanes];
        });
        
      } catch (error) {
        console.error('Error fetching aircraft in App component:', error);
      }
    };

    fetchAircraft();
  }, []); // Empty dependency array - run once on mount

  // Update plane positions
  useEffect(() => {
    let lastTime = performance.now(); // Initialize lastTime

    const interval = setInterval(() => {
      const currentTime = performance.now();
      const dt = (currentTime - lastTime) / 1000;
      lastTime = currentTime;
      
      setPlanes((prevPlanes) =>
        prevPlanes
          .filter((p) => p && p.callsign && p.speed != null && p.lat != null && p.lng != null && p.heading != null)
          .map((plane) => {
            const speedKms = (plane.speed * 1.852) / 3600;
            const headingRad = (plane.heading * Math.PI) / 180;
            const earthRadius = 6371;

            //Calculate new position using simple equirectangular approximation  (proiectie cilindrica echidistanta)
            const deltaLat = (speedKms * Math.cos(headingRad) * dt) / earthRadius;
            const deltaLon =
              (speedKms * Math.sin(headingRad) * dt) /
              (earthRadius * Math.cos((plane.lat * Math.PI) / 180));

            const newLat = plane.lat + (deltaLat * 180) / Math.PI;
            const newLng = plane.lng + (deltaLon * 180) / Math.PI;

            return {
              ...plane,
              lat: newLat,
              lng: newLng,
              trail: [...plane.trail, [newLat, newLng]].slice(-50),
            };
          })
      );
    }, 4000); // Update every 4 seconds

    return () => clearInterval(interval);
  }, []);

  // Update ref when planes change
  useEffect(() => {
    planesRef.current = planes;
  }, [planes]);

  // Filter out any invalid planes before passing to components
  const validPlanes = planes.filter(plane => 
    plane && plane.callsign && plane.lat != null && plane.lng != null
  );


  return (
    <div style={{ height: "100vh", width: "100vw" }}>
      <WeatherControls
        showClouds={showClouds}
        setShowClouds={setShowClouds}
        showWind={showWind}
        setShowWind={setShowWind}
        showPressure={showPressure}
        setShowPressure={setShowPressure}
      />

      <button
        style={{ position: "absolute", top: 80, left: 10, zIndex: 1000 }}
        onClick={() => setRole(role === "pilot" ? "controller" : "pilot")}
      >
        Switch to {role === "pilot" ? "Controller" : "Pilot"} View
      </button>

      {role === "pilot" && selectedCallsign && (
        <PilotATCPanel
          plane={validPlanes.find((p) => p.callsign === selectedCallsign)}
          onAccept={(callsign) => alert(`Accepted command for ${callsign}`)}
          onDeny={(callsign) => alert(`Denied command for ${callsign}`)}
        />
      )}

      {role === "controller" && selectedCallsign && (
        <CommandPanel
          plane={validPlanes.find((p) => p.callsign === selectedCallsign)}
          onSendCommand={(callsign, type, value) =>
            alert(`Sent command to ${callsign}: ${type} -> ${value}`)
          }
        />
      )}

      <AirspaceMap
        planes={validPlanes}
        selectedCallsign={selectedCallsign}
        setSelectedCallsign={setSelectedCallsign}
        showClouds={showClouds}
        showWind={showWind}
        showPressure={showPressure}
      />
    </div>
  );
}

export default App;