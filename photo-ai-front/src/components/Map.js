import React, { useState, useEffect, useCallback } from 'react';
import { GoogleMap, LoadScript, Marker } from '@react-google-maps/api';

const containerStyle = {
  width: '100%',
  height: '400px'
};

const MapComponent = ({ onLocationSelect, center }) => {
  const [position, setPosition] = useState(null);
  const [initialLocationFetched, setInitialLocationFetched] = useState(false); 

  const handleMapClick = (event) => {
    const latLng = event.latLng.toJSON();
    setPosition(latLng);
    onLocationSelect(latLng);
  };

  const handleLocationSuccess = useCallback((position) => {
    if (!initialLocationFetched) { 
      const { latitude, longitude } = position.coords;
      onLocationSelect({ lat: latitude, lng: longitude });
      setInitialLocationFetched(true);
    }
  }, [onLocationSelect, initialLocationFetched]);

  const handleLocationError = useCallback(() => {
    console.error('Failed to retrieve user location');
  }, []);

  useEffect(() => {
    if (!initialLocationFetched && navigator.geolocation) { 
      navigator.geolocation.getCurrentPosition(handleLocationSuccess, handleLocationError);
    }
  }, [handleLocationSuccess, handleLocationError, initialLocationFetched]);

  return (
    <LoadScript googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY} libraries={['places']}>
      <GoogleMap
        mapContainerStyle={containerStyle}
        center={center}
        zoom={10}
        onClick={handleMapClick}
      >
        {position && <Marker position={position} />}
      </GoogleMap>
    </LoadScript>
  );
};

export default MapComponent;
