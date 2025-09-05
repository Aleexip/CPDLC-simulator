import React from 'react';

// WeatherControls component for CPDLC aviation simulator
// Should render toggle buttons for: clouds, wind, pressure weather layers
// Props: weatherState object with boolean values, onWeatherToggle callback function
// Use modern React functional component with hooks
// Style buttons with conditional active state (blue when active, dark when inactive)
// Should be responsive and accessible

const weatherTypes = [
    { key: 'clouds', label: 'Clouds' },
    { key: 'wind', label: 'Wind' },
    { key: 'pressure', label: 'Pressure' },
];

const WeatherControls = ({ weatherState, onWeatherToggle }) => (
    <div
        className="weather-controls-panel"
        style={{
            position: 'absolute',
            top: 16,
            left: 16,
            zIndex: 1000,
            background: '#222',
            padding: '12px 16px',
            borderRadius: '8px',
            display: 'flex',
            gap: '12px',
            boxShadow: '0 2px 8px rgba(0,0,0,0.2)',
        }}
        role="group"
        aria-label="Weather Layer Controls"
    >
        {weatherTypes.map(({ key, label }) => (
            <button
                key={key}
                type="button"
                aria-pressed={weatherState[key]}
                onClick={() => onWeatherToggle(key)}
                className={`weather-toggle-btn${weatherState[key] ? ' active' : ''}`}
                style={{
                    background: weatherState[key] ? '#1976d2' : '#333',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '4px',
                    padding: '8px 16px',
                    cursor: 'pointer',
                    fontWeight: 'bold',
                    outline: 'none',
                    transition: 'background 0.2s',
                }}
            >
                {label}
            </button>
        ))}
    </div>
);

export default WeatherControls;