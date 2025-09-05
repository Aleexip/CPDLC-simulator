// Aircraft service for CPDLC simulator
// Handles API calls to backend running on localhost:8081
// Functions needed: fetchAircraft() - gets all aircraft from /api/aircraft endpoint
// Should handle loading states and errors
// Return aircraft array with callsign, latitude, longitude, altitude
// Use modern async/await with fetch API

const API_BASE_URL = 'http://localhost:8081/api';

export const aircraftService = {
  // Service methods implementation
    fetchAircraft: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/aircraft`);
        if (!response.ok) {
        throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data; // Assuming data is an array of aircraft
    } catch (error) {
        console.error('Error fetching aircraft:', error);
        throw error; // Re-throw to handle it in the calling code
    }
    },
};
