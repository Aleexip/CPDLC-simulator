// CommandPanel.js
import React, { useState } from "react";

function CommandPanel({ plane, onSendCommand }) {
  const [customValues, setCustomValues] = useState({
    heading: "",
    altitude: "",
    speed: "",
  });

  if (!plane)
    return (
      <div
        style={{
          position: "fixed",
          top: 150,
          right: 10,
          zIndex: 1000,
          color: "#fff",
        }}
      >
        Select an aircraft
      </div>
    );

  // Preset values
  const headingOptions = [
    plane.heading + 10,
    plane.heading - 10,
    plane.heading + 30,
    "Custom",
  ];
  const altitudeOptions = [
    plane.flight_level + 1000,
    plane.flight_level - 1000,
    plane.flight_level + 5000,
    "Custom",
  ];
  const speedOptions = [
    plane.speed + 50,
    plane.speed - 50,
    plane.speed + 100,
    "Custom",
  ];

  const handleSend = (type, value) => {
    if (value === "Custom") value = customValues[type];
    if (value !== "" && !isNaN(value))
      onSendCommand(plane.callsign, type, Number(value));
    setCustomValues((prev) => ({ ...prev, [type]: "" }));
  };

  // Styles
  const buttonStyle = (type) => ({
    margin: "2px 2px",
    padding: "4px 8px",
    borderRadius: 4,
    border: "none",
    cursor: "pointer",
    backgroundColor:
      type === "heading"
        ? "#3b82f6"
        : type === "altitude"
        ? "#10b981"
        : "#f97316",
    color: "#fff",
    fontWeight: "bold",
    transition: "0.2s",
  });

  const customInputStyle = {
    width: "60px",
    marginLeft: 5,
    borderRadius: 4,
    border: "2px solid #facc15",
    padding: "2px 4px",
  };

  const panelStyle = {
    position: "fixed",
    top: 150,
    right: 10,
    zIndex: 1000,
    background: "#222",
    color: "#fff",
    padding: 15,
    borderRadius: 8,
    width: 270,
    boxShadow: "0 0 10px rgba(0,0,0,0.5)",
  };

  return (
    <div style={panelStyle}>
      <h4 style={{ marginBottom: 10 }}>Controller Panel</h4>
      <div style={{ marginBottom: 15 }}>
        <strong>Selected:</strong> {plane.callsign}
      </div>

      {/* Heading Controls */}
      <div style={{ marginBottom: 10 }}>
        <strong>Heading:</strong>
        {headingOptions.map((val, idx) =>
          val === "Custom" ? (
            <input
              key={idx}
              type="number"
              placeholder="Custom"
              value={customValues.heading}
              onChange={(e) =>
                setCustomValues({ ...customValues, heading: e.target.value })
              }
              onBlur={() => handleSend("heading", "Custom")}
              style={customInputStyle}
            />
          ) : (
            <button
              key={idx}
              onClick={() => handleSend("heading", val)}
              style={buttonStyle("heading")}
            >
              {val}°
            </button>
          )
        )}
      </div>

      {/* Altitude Controls */}
      <div style={{ marginBottom: 10 }}>
        <strong>Altitude:</strong>
        {altitudeOptions.map((val, idx) =>
          val === "Custom" ? (
            <input
              key={idx}
              type="number"
              placeholder="Custom"
              value={customValues.altitude}
              onChange={(e) =>
                setCustomValues({ ...customValues, altitude: e.target.value })
              }
              onBlur={() => handleSend("altitude", "Custom")}
              style={customInputStyle}
            />
          ) : (
            <button
              key={idx}
              onClick={() => handleSend("altitude", val)}
              style={buttonStyle("altitude")}
            >
              {val}
            </button>
          )
        )}
      </div>

      {/* Speed Controls */}
      <div style={{ marginBottom: 10 }}>
        <strong>Speed:</strong>
        {speedOptions.map((val, idx) =>
          val === "Custom" ? (
            <input
              key={idx}
              type="number"
              placeholder="Custom"
              value={customValues.speed}
              onChange={(e) =>
                setCustomValues({ ...customValues, speed: e.target.value })
              }
              onBlur={() => handleSend("speed", "Custom")}
              style={customInputStyle}
            />
          ) : (
            <button
              key={idx}
              onClick={() => handleSend("speed", val)}
              style={buttonStyle("speed")}
            >
              {val}
            </button>
          )
        )}
      </div>

      {/* Command History */}
      <div style={{ marginTop: 15, maxHeight: 150, overflowY: "auto" }}>
        <h5>Command History:</h5>
        <ul style={{ paddingLeft: 15 }}>
          {plane.commandsHistory?.map((cmd, idx) => (
            <li key={idx}>
              {cmd.type}: {cmd.value} ({cmd.status})
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default CommandPanel;
