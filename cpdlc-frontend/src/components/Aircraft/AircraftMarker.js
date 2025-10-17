// AircraftMarker.js
import React, {useState} from "react";
import { Marker, Tooltip, Polyline } from "react-leaflet";

function PlaneMarker({ plane, planeIcon, selectedCallsign, setSelectedCallsign }) {


    const [status, setStatus] = useState(plane.status || "pending");
    const [isHovered, setIsHovered] = useState(false);

     if (!plane || plane.lat == null || plane.lng == null || plane.heading == null || plane.speed ==null) return null;
  return (
    <>
      <Marker
        key={plane.callsign}
        position={[plane.lat, plane.lng]}
        icon={planeIcon}
        rotationAngle={plane.heading}
        eventHandlers={{
            click: () => setSelectedCallsign(plane.callsign),
        }}
      >
    
        {/* Tooltip with aircraft details */}

       <Tooltip direction="top" offset={[0, -10]} opacity={1}>
        <div style={{ userSelect: "none" }}>
            <strong>Callsign:</strong> {plane.callsign}<br />
            <strong>Aircraft Type:</strong> {plane.aircraftType}<br />
            <strong>Flight Level:</strong> FL{plane.flightLevel}<br />
            <strong>Speed:</strong> {plane.speed} knots<br />
            <strong>Heading:</strong> {plane.heading}<br />
            <strong>Status: </strong>{plane.currentPhase}<br />
            <strong>Airline: </strong>{plane.airline}<br />
            <strong>Departure: </strong>{plane.departureAirport}<br />
            <strong>Arrival: </strong>{plane.arrivalAirport}<br />
        </div>
        
        </Tooltip>
      </Marker>

      {/* Plane trails */}
      {plane.callsign === selectedCallsign && (
        <Polyline
          key={plane.callsign + "-trail"}
          positions={plane.trail}
          color="yellow"
          weight={5}
        />
      )}
    </>
  );
}

export default PlaneMarker;
