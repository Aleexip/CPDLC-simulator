// Custom React hook for managing aircraft data in CPDLC simulator
// Should provide: aircraft state, loading state, error state
// Functions: fetchAircraft, updateAircraftPosition
// Use aircraftService for API calls
// Handle automatic refresh every 5 seconds
// Return object with: { aircraft, loading, error, fetchAircraft, refreshInterval }

import { useState, useEffect, useCallback } from 'react';
import { aircraftService } from '../services/aircraftService';

export const useAircraft = () => {
  // Hook implementation
    const [aircraft, setAircraft] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const fetchAircraft = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const data = await aircraftService.fetchAircraft();
            setAircraft(data);
        }
        catch (err) {
            setError(err.message || 'Error fetching aircraft data');
            setAircraft([]);
        }
        finally {
            setLoading(false);
        }
    }, []);
    useEffect(() => {
        fetchAircraft(); // Initial fetch
        const interval = setInterval(fetchAircraft, 5000); // Refresh every 5 seconds
        return () => clearInterval(interval); // Cleanup on unmount
    }, [fetchAircraft]);
    return { aircraft, loading, error, fetchAircraft };
};
