import React, { useState, useEffect, useRef, useCallback } from 'react';
import { GoogleMap, LoadScript, Marker } from '@react-google-maps/api';

const containerStyle = {
  width: '100%',
  height: '400px'
};

const MapComponent = ({ onLocationSelect, center, markerPosition, onLocationSuccess }) => {
  const [position, setPosition] = useState(markerPosition);
  const mapRef = useRef(null);
  const initialLocationFetched = useRef(false);

  const handleMapClick = (event) => {
    const latLng = event.latLng.toJSON();
    setPosition(latLng);
    onLocationSelect(latLng);
  };

  const handleLocationSuccess = useCallback((position) => {
    if (!initialLocationFetched.current) {
      const { latitude, longitude } = position.coords;
      onLocationSuccess({ lat: latitude, lng: longitude });
      initialLocationFetched.current = true;
    }
  }, [onLocationSuccess]);

  const handleLocationError = useCallback(() => {
    console.error('Failed to retrieve user location');
  }, []);

  useEffect(() => {
    if (navigator.geolocation && !initialLocationFetched.current) {
      navigator.geolocation.getCurrentPosition(handleLocationSuccess, handleLocationError);
    }
  }, [handleLocationSuccess, handleLocationError]);

  useEffect(() => {
    if (markerPosition) {
      setPosition(markerPosition);
    } else {
      setPosition(null);
    }
  }, [markerPosition]);

  useEffect(() => {
    if (mapRef.current) {
      mapRef.current.panTo(center);
    }
  }, [center]);

  return (
    <LoadScript googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY} libraries={['places']}>
      <GoogleMap
        mapContainerStyle={containerStyle}
        center={center}
        zoom={10}
        onClick={handleMapClick}
        onLoad={map => (mapRef.current = map)}
      >
        {position && <Marker position={position} />}
      </GoogleMap>
    </LoadScript>
  );
};

export default MapComponent;
