// MapContainer component for CPDLC aviation map
// Wraps react-leaflet MapContainer with default settings for aviation
// Center on Romania (lat: 45.9432, lng: 24.9668), zoom: 7
// Include base tile layer and children components
// Handle map instance reference for child components
// Props: children (weather layers, aircraft markers)

import React from 'react';
import { MapContainer as LeafletMap, TileLayer } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

const MapContainer = ({ children }) => {
  // Component implementation
    return (
        <LeafletMap
            center={[45.9432, 24.9668]} // Center on Romania
            zoom={7}
            style={{ height: '100vh', width: '100%' }}  // Full viewport size
            scrollWheelZoom={true}
        >       
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            />
            {children}
        </LeafletMap>
    );
}   