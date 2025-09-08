import React, { useEffect, useState } from "react"; // Import React and necessary hooks for state management

import "leaflet/dist/leaflet.css"; // Import Leaflet CSS for proper map styling

import WeatherLayer from "./components/Weather/WeatherLayer"; // Import the WeatherLayer component for weather overlays
import WeatherControls from "./components/Weather/WeatherControls"; // Import the WeatherControls component for weather toggles
import PlaneMarker from "./components/Aircraft/AircraftMarker"; // Import the PlaneMarker component for individual plane markers
import MapContainer from "./components/Map/MapContainer"; // Import the MapContainer component for the main map
import AirspaceMap from "./components/Map/MapContainer"; // Import the AirspaceMap component for the main map
import PilotATCPanel from "./components/Controller/PilotAtcPanel"; // Import the PilotATCPanel component for pilot-ATC interactions
import ControllerATCPanel from "./components/Controller/CommandPanel"; // Import the ControllerATCPanel component for controller commands
import CommandPanel from "./components/Controller/CommandPanel";

function App() {
  // State for each weather overlays
  const [showClouds, setShowClouds] = useState(false);
  const [showWind, setShowWind] = useState(false);
  const [showPressure, setShowPressure] = useState(false);

  const [role, setRole] = useState("controller"); // "controller" or "pilot"

  const [selectedCallsign, setSelectedCallsign] = useState(null); // Currently selected aircraft callsign

  // Sample plane data
  const [planes, setPlanes] = useState([
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
  ]);

  useEffect(() => {
    // Simulate fetching plane data every 1 seconds
    const interval = setInterval(() => {
      // Here would fetch real data from an API

      setPlanes((prevPlanes) =>
        prevPlanes.map((plane) => {
          // Calculate movement
          const speedKms = (plane.speed * 1.852) / 3600; // knots to km/s
          const headingRad = (plane.heading * Math.PI) / 180;
          const earthRadius = 6371; // km
          const deltaLat = (speedKms * Math.cos(headingRad)) / earthRadius;
          const deltaLon =
            (speedKms * Math.sin(headingRad)) /
            (earthRadius * Math.cos((plane.lat * Math.PI) / 180));
          return {
            ...plane,
            lat: plane.lat + (deltaLat * 180) / Math.PI,
            lng: plane.lng + (deltaLon * 180) / Math.PI,
            trail: [...plane.trail, [plane.lat, plane.lng]].slice(-350), // Keep last 350 positions
          };
        })
      );
    }, 1000);

    return () => clearInterval(interval);
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
