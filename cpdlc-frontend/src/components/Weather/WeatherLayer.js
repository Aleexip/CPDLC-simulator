import React, { useRef } from "react";
import { useMap } from "react-leaflet";
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
export default WeatherLayer;