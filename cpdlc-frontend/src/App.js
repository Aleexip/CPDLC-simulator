import React, { useEffect, useRef, useState } from "react"; // Import React and necessary hooks for state management
import { MapContainer, TileLayer, useMap } from "react-leaflet"; // Import necessary components from react-leaflet for map rendering
import "leaflet/dist/leaflet.css"; // Import Leaflet CSS for proper map styling
import { Marker, Popup, Polyline } from "react-leaflet"; // Import Marker and Popup for plane markers
import L from "leaflet"; // Import Leaflet for map functionalities
import "leaflet-rotatedmarker"; // Import rotated marker plugin for heading support


// Helper component to add/remove weather overlays
function WeatherLayer({ type, visible }) {
  const map = useMap();
  const layerRef = useRef();

  React.useEffect(() => {
    if (!visible) {
      if (layerRef.current) {
        map.removeLayer(layerRef.current);
        layerRef.current = null;
      }
      return;
    }

    let url = "";
   if (type === "clouds")
    url = `http://localhost:8081/api/weather-tile/clouds_new/{z}/{x}/{y}.png`;
  if (type === "wind")
    url = `http://localhost:8081/api/weather-tile/wind_new/{z}/{x}/{y}.png`;
  if (type === "pressure")
    url = `http://localhost:8081/api/weather-tile/pressure_new/{z}/{x}/{y}.png`;

    // Create the weather tile layer
    const L = window.L || require("leaflet");
    const weatherLayer = L.tileLayer(url, {
      opacity: 1,
      attribution: 'Map data &copy; <a href="http://openweathermap.org">OpenWeatherMap</a>',
      maxZoom: 19,
    });

    weatherLayer.addTo(map);
    layerRef.current = weatherLayer;

    // Cleanup on unmount or when toggling
    return () => {
      if (layerRef.current) {
        map.removeLayer(layerRef.current);
        layerRef.current = null;
      }
    };
  }, [type, visible, map]);

  return null;
}

function App() {

  // State for each weather overlay
  const [showClouds, setShowClouds] = useState(false);
  const [showWind, setShowWind] = useState(false);
  const [showPressure, setShowPressure] = useState(false);
  const [selectedCallsign, setSelectedCallsign] = useState(null);

  // Sample plane data
  const [planes, setPlanes] = useState([
    { icao: "LZIB", flight_level: 350, lat: 44.57, lng: 27.48, heading: 90, speed: 450, callsign: "BUL123",trail: [[44.57,27.48]] },
    {icao: "A320", flight_level: 320, lat: 44.87, lng: 26.48, heading: 270, speed: 430, callsign: "BUL456", trail: [[44.87,26.48]] },
    {icao: "B737", flight_level: 300, lat: 44.57, lng: 25.48, heading: 180, speed: 400, callsign: "BUL789",trail: [[44.57,25.48]] },
    {icao: "C172", flight_level: 100, lat: 45.57, lng: 24.48, heading: 0, speed: 120, callsign: "BUL101", trail: [[45.57,24.48]]},
    {icao: "E190", flight_level: 280, lat: 45.57, lng: 23.48, heading: 45, speed: 500, callsign: "BUL202", trail: [[45.57,23.48]]},

  ]);

  // Define a custom icon for the planes
  const planeIcon = new L.Icon({
    iconUrl: "https://icones.pro/wp-content/uploads/2021/08/symbole-d-avion-et-de-voyage-jaune.png", 
    iconSize: [32, 32], 
    iconAnchor: [16, 16], 

  });

  useEffect(() => {
    // Simulate fetching plane data every 1 seconds
    const interval = setInterval(() => {
      // Here would fetch real data from an API

      setPlanes(prevPlanes =>
  prevPlanes.map(plane => {
    // Calculate movement
    const speedKms = plane.speed * 1.852 / 3600; // knots to km/s
    const headingRad = (plane.heading * Math.PI) / 180;
    const earthRadius = 6371; // km
    const deltaLat = (speedKms * Math.cos(headingRad)) / earthRadius;
    const deltaLon = (speedKms * Math.sin(headingRad)) / (earthRadius * Math.cos(plane.lat * Math.PI / 180));
    return {
      ...plane,
      lat: plane.lat + (deltaLat * 180) / Math.PI,
      lng: plane.lng + (deltaLon * 180) / Math.PI,
      trail: [...plane.trail, [plane.lat, plane.lng]].slice(-250) // Keep last 10 positions

     
    };
  })
);
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div style={{ height: "100vh", width: "100vw" }}>
      {/* Weather control buttons */}
      <div style={{
        position: "absolute", zIndex: 1000, top: 10, left: 10, background: "rgba(30,30,30,0.8)", padding: 10, borderRadius: 8
      }}>
        <button onClick={() => setShowClouds(v => !v)} style={{marginRight: 8, background: showClouds ? "#007bff" : "#222", color: "#fff", border: "none", padding: "8px 12px", borderRadius: 4}}>
          {showClouds ? "Hide Clouds" : "Show Clouds"}
        </button>
        <button onClick={() => setShowWind(v => !v)} style={{marginRight: 8, background: showWind ? "#007bff" : "#222", color: "#fff", border: "none", padding: "8px 12px", borderRadius: 4}}>
          {showWind ? "Hide Wind" : "Show Wind"}
        </button>
        <button onClick={() => setShowPressure(v => !v)} style={{background: showPressure ? "#007bff" : "#222", color: "#fff", border: "none", padding: "8px 12px", borderRadius: 4}}>
          {showPressure ? "Hide Pressure" : "Show Pressure"}
        </button>
      </div>
      {/* Map with CartoDB Dark Matter as base */}
      <MapContainer center={[46, 25]} zoom={7.5} style={{ height: "100%", width: "100%" }}>
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


        {/* Plane trails */}
        {planes.map((plane) =>
  plane.callsign === selectedCallsign ? (
    <Polyline
      key={plane.callsign + "-trail"}
      positions={plane.trail}
      color="yellow"
      weight={5}
    />
  ) : null
)}


        {/* Plane markers */} 
        {planes.map((plane, idx) => (
  <Marker
  key={plane.callsign}
  position={[plane.lat, plane.lng]}
  icon={planeIcon}
  rotationAngle={plane.heading}
>
  <Popup
    eventHandlers={{
      add: () => setSelectedCallsign(plane.callsign),
      remove: () => setSelectedCallsign(null),
    }}
  >
    <div>
      <strong>Callsign:</strong> {plane.callsign}<br />
      <strong>ICAO:</strong> {plane.icao}<br />
      <strong>Flight Level:</strong> FL{plane.flight_level}<br />
      <strong>Speed:</strong> {plane.speed} knots<br />
      <strong>Heading:</strong> {plane.heading}°
    </div>
  </Popup>
</Marker>
        ))}
      </MapContainer>
    </div>
  );
}

export default App;