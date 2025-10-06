import React, { useEffect, useState, useRef} from "react"; // Import React and necessary hooks for state management

import "leaflet/dist/leaflet.css"; // Import Leaflet CSS for proper map styling

import WeatherLayer from "./components/Weather/WeatherLayer"; // Import the WeatherLayer component for weather overlays
import WeatherControls from "./components/Weather/WeatherControls"; // Import the WeatherControls component for weather toggles
import PlaneMarker from "./components/Aircraft/AircraftMarker"; // Import the PlaneMarker component for individual plane markers
import MapContainer from "./components/Map/MapContainer"; // Import the MapContainer component for the main map
import AirspaceMap from "./components/Map/MapContainer"; // Import the AirspaceMap component for the main map
import PilotATCPanel from "./components/Controller/PilotAtcPanel"; // Import the PilotATCPanel component for pilot-ATC interactions
import ControllerATCPanel from "./components/Controller/CommandPanel"; // Import the ControllerATCPanel component for controller commands
import CommandPanel from "./components/Controller/CommandPanel";

import { aircraftService } from './services/aircraftService';


function App() {
  // State for each weather overlays
  const [showClouds, setShowClouds] = useState(false);
  const [showWind, setShowWind] = useState(false);
  const [showPressure, setShowPressure] = useState(false);

  const [role, setRole] = useState("controller"); // "controller" or "pilot"
  const [selectedCallsign, setSelectedCallsign] = useState(null); // Currently selected aircraft callsign
  
  // Sample plane data
  const [planes, setPlanes] = useState([
    /*
    {
      icao: "LZIB",
      flight_level: 350,
      lat: 44.57,
      lng: 27.48,
      heading: 90,
      speed: 450,
      callsign: "BUL123",
      trail: [[44.57, 27.48]],
    },
    {
      icao: "A320",
      flight_level: 320,
      lat: 44.87,
      lng: 26.48,
      heading: 270,
      speed: 430,
      callsign: "BUL456",
      trail: [[44.87, 26.48]],
    },
    {
      icao: "B737",
      flight_level: 300,
      lat: 44.57,
      lng: 25.48,
      heading: 180,
      speed: 400,
      callsign: "BUL789",
      trail: [[44.57, 25.48]],
    },
    {
      icao: "C172",
      flight_level: 100,
      lat: 45.57,
      lng: 24.48,
      heading: 0,
      speed: 120,
      callsign: "BUL101",
      trail: [[45.57, 24.48]],
    },
    {
      icao: "E190",
      flight_level: 280,
      lat: 45.57,
      lng: 23.48,
      heading: 45,
      speed: 500,
      callsign: "BUL202",
      trail: [[45.57, 23.48]],
    }, 
    */
  ]);
   // Ref to hold the latest planes state
  const planesRef = useRef([]);
  planesRef.current = planes;

  // Fetch aircraft data from backend on component mount
  useEffect(() => {
  const fetchAircraft = async () => {
    try {
      const data = await aircraftService.fetchAircraft();
      const planesFromBackend = data.map((plane) => ({
        ...plane,
        lat: plane.latitude,
        lng: plane.longitude,
        heading: plane.heading,
        trail: [[plane.latitude, plane.longitude]], 
      }));
      
      let index = 0;
      const spwanInterval = setInterval(() => {
        if(index >= planesFromBackend.length) return clearInterval(spwanInterval);
        setPlanes(prev=> [...prev, planesFromBackend[index]]);
        index++;
      }, 200); // spawn a new plane every 2 seconds
      } catch (error) 
      {
        console.error('Error fetching aircraft in App component:', error);
      }
    };

  fetchAircraft();
}, []);

  useEffect(() => {
    let animationFrameId;
  /* let lastTime = performance.now();

const updatePositions = (currentTime) => {
  const dt = (currentTime - lastTime) / 1000; // secunde
  lastTime = currentTime;

  setPlanes(prevPlanes =>
    prevPlanes.map(plane => {
      const speedKms = (plane.speed * 1.852) / 3600; // km/s
      const headingRad = (plane.heading * Math.PI) / 180;
      const earthRadius = 6371;

      const deltaLat = (speedKms * Math.cos(headingRad) * dt) / earthRadius;
      const deltaLon = (speedKms * Math.sin(headingRad) * dt) / (earthRadius * Math.cos((plane.lat * Math.PI) / 180));

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

  requestAnimationFrame(updatePositions);
};

requestAnimationFrame(updatePositions);

*/
    const updatePositions = () => {
      setPlanes((prevPlanes) =>
        prevPlanes.map((plane) => {
          // Simple movement logic: move in the direction of heading at speed
          const speedKms = (plane.speed * 1.852) / 3600; // knots -> km/s
          const headingRad = (plane.heading * Math.PI) / 180;
          const earthRadius = 6371;

          // Calculate new position using  the haversine formula approximation
          const deltaLat = (speedKms * Math.cos(headingRad)) / earthRadius;
          const deltaLon =
            (speedKms * Math.sin(headingRad)) / (earthRadius * Math.cos((plane.lat * Math.PI) / 180));

            // Update lat/lon in degrees from radians
          const newLat = plane.lat + (deltaLat * 180) / Math.PI;
          const newLng = plane.lng + (deltaLon * 180) / Math.PI;

          return {
            ...plane,
            lat: newLat,
            lng: newLng,
            trail: [...plane.trail, [newLat, newLng]].slice(-50), // keep last 50 positions
          };
        })
      );

      animationFrameId = requestAnimationFrame(updatePositions);
    };

    animationFrameId = requestAnimationFrame(updatePositions);
    return () => cancelAnimationFrame(animationFrameId);
  }, []);
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

      {/*Role toggle*/}
      <button
        style={{ position: "absolute", top: 80, left: 10, zIndex: 1000 }}
        onClick={() => setRole(role === "pilot" ? "controller" : "pilot")}
      >
        Switch to {role === "pilot" ? "Controller" : "Pilot"} View
      </button>

    
      {/* Pilot  Panel */}
      {role === "pilot" && (
        <PilotATCPanel
          plane={planes.find((p) => p.callsign === selectedCallsign)}
          onAccept={(callsign) => alert(`Accepted command for ${callsign}`)}
          onDeny={(callsign) => alert(`Denied command for ${callsign}`)}
        />
      )}

      {/* Controller Panel */}
      {role === "controller" && (
        <CommandPanel
          plane={planes.find((p) => p.callsign === selectedCallsign)}
          onSendCommand={(callsign, type, value) =>
            alert(`Sent command to ${callsign}: ${type} -> ${value}`)
          }
        />
      )}

      {/* Main Map */}
      <AirspaceMap
        planes={planes}
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
