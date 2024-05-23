import React, { useRef, useEffect } from 'react';
import { LoadScript } from '@react-google-maps/api';

const PlacesAutocomplete = ({ onPlaceSelected }) => {
  const inputRef = useRef(null);

  useEffect(() => {
    const loadAutocomplete = () => {
      if (window.google && window.google.maps && window.google.maps.places) {
        const autocomplete = new window.google.maps.places.Autocomplete(inputRef.current, {
          types: ['geocode']
        });

        autocomplete.addListener('place_changed', () => {
          const place = autocomplete.getPlace();
          if (place.geometry) {
            onPlaceSelected({
              lat: place.geometry.location.lat(),
              lng: place.geometry.location.lng(),
            });
          }
        });
      }
    };

    loadAutocomplete();
  }, [onPlaceSelected]);

  return (
    <LoadScript googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY} libraries={['places']}>
      <input ref={inputRef} type="text" placeholder="Enter a location" style={{ width: '100%', padding: '10px', margin: '10px 0' }} />
    </LoadScript>
  );
};

export default PlacesAutocomplete;
