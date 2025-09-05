// WeatherControls.js

function WeatherControls({ showClouds, setShowClouds, showWind, setShowWind, showPressure, setShowPressure }) {
    // Weather control buttons
  return (
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
  );
}

export default WeatherControls;
