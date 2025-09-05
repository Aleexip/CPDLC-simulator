import React from "react"; // Import React for component creation

import { MapContainer, TileLayer } from "react-leaflet"; // Import MapContainer and TileLayer for map rendering
import L from "leaflet"; // Import Leaflet for map functionalities
import "leaflet-rotatedmarker"; // Import rotated marker plugin for heading support


import WeatherLayer from "../Weather/WeatherLayer"; // Import the WeatherLayer component for weather overlays
import PlaneMarker from "../Aircraft/AircraftMarker"; // Import the PlaneMarker component for individual plane markers

// fix for leaflet icon issues in some build environments
if (typeof window !== "undefined") {
  window.L = L;
}

function AirspaceMap({ 
  planes, 
  role,
  selectedCallsign, 
  setSelectedCallsign, 
  showClouds, 
  showWind, 
  showPressure 
}) {
     // Define a custom icon for the planes
      const planeIcon = new L.Icon({
        iconUrl: "https://icones.pro/wp-content/uploads/2021/08/symbole-d-avion-et-de-voyage-jaune.png", 
        iconSize: [32, 32], 
        iconAnchor: [16, 16], 
    
      });

      //Filter planes based on role (plot sees only own planes)
      const visibilePlanes = role === "pilot" ? planes.filter(p => p.callsign.startsWith("BUL123")) : planes;

  return (

    <MapContainer center={[46, 25]} zoom={7.5} style={{ height: "100%", width: "100%" }}>
      {/* Basemap */}
      <TileLayer
        url="https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>'
        subdomains="abcd"
        maxZoom={20}
      />

        
      {/* Weather overlays */}
      <WeatherLayer type="clouds" visible={showClouds} />
      <WeatherLayer type="wind" visible={showWind} />
      <WeatherLayer type="pressure" visible={showPressure} />

      {/* Plane markers + trails */}
      {planes.map(plane => (
        <PlaneMarker
          key={plane.callsign}
          plane={plane}
          planeIcon={planeIcon}
          selectedCallsign={selectedCallsign}
          setSelectedCallsign={setSelectedCallsign}
        />
      ))}
    </MapContainer>
  );
}

export default AirspaceMap;
