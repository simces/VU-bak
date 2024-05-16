import React, { useEffect, useRef, useMemo, useCallback } from 'react';
import useGoogleMaps from '../utils/useGoogleMaps';

const mapContainerStyle = {
  width: '500px',
  height: '400px',
};

const LocationMapModal = ({ isOpen, toggleModal, latitude, longitude, location }) => {
  const isLoaded = useGoogleMaps(process.env.REACT_APP_GOOGLE_MAPS_API_KEY);
  const mapRef = useRef(null);
  const markerRef = useRef(null);

  const center = useMemo(() => ({
    lat: latitude,
    lng: longitude,
  }), [latitude, longitude]);

  const initializeMap = useCallback(() => {
    if (isLoaded && isOpen && mapRef.current) {
      const map = new window.google.maps.Map(mapRef.current, {
        center,
        zoom: 15,
      });

      const marker = new window.google.maps.Marker({
        position: center,
        map,
        title: location,
        animation: window.google.maps.Animation.DROP,
      });

      markerRef.current = marker;
    }
  }, [isLoaded, isOpen, center, location]);

  useEffect(() => {
    if (isOpen) {
      initializeMap();
    }
    return () => {
      if (markerRef.current) {
        markerRef.current.setMap(null);
        markerRef.current = null;
      }
    };
  }, [isOpen, initializeMap]);

  return isOpen ? (
    <div style={{ position: 'fixed', top: '20%', left: '30%', background: 'white', padding: '20px', zIndex: 1000 }}>
      <button onClick={toggleModal} style={{ float: 'right', marginBottom: '10px' }}>Close</button>
      <div ref={mapRef} style={mapContainerStyle}></div>
      <p>{location}</p>
    </div>
  ) : null;
};

export default LocationMapModal;
