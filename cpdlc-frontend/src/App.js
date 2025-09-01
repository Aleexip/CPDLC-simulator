import React, { useRef, useState } from "react";
import { MapContainer, TileLayer, useMap, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";


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

// Example plane icon (replace with your own SVG or image)
const planeIcon = new L.Icon({
  iconUrl: "https://cdn-icons-png.flaticon.com/512/684/684908.png",
  iconSize: [32, 32],
  iconAnchor: [16, 16],
});

function App() {
  // State for each weather overlay
  const [showClouds, setShowClouds] = useState(false);
  const [showWind, setShowWind] = useState(false);
  const [showPressure, setShowPressure] = useState(false);

  // Example: state for planes
  const [planes, setPlanes] = useState([
    { id: 1, lat: 46.5, lng: 25.5, heading: 90 },
    { id: 2, lat: 45.8, lng: 24.8, heading: 45 },
    // Add more planes as needed
  ]);

  // Optionally, update plane positions over time
  // useEffect(() => {
  //   const interval = setInterval(() => {
  //     setPlanes(prev =>
  //       prev.map(plane => ({
  //         ...plane,
  //         lng: plane.lng + 0.01, // Example movement
  //       }))
  //     );
  //   }, 1000);
  //   return () => clearInterval(interval);
  // }, []);

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
          {showPre