import React from "react";

function PilotATCPanel({ plane, onAccept, onDeny }) {
  if (!plane) return null;

  return (
    <div style={{
      position: "absolute",
      top: 100,
      right: 10,
      background: "rgba(0,0,0,0.8)",
      color: "#fff",
      padding: 10,
      borderRadius: 8,
      zIndex: 1000,
    }}>
      <h4>{plane.callsign} - Commands</h4>
      <button onClick={() => onAccept(plane.callsign)}>Accept</button>
      <button onClick={() => onDeny(plane.callsign)}>Deny</button>
    </div>
  );
}

export default PilotATCPanel;
