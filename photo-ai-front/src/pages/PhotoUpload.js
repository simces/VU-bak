import React, { useState, useEffect } from 'react';
import fetchWithToken from '../utils/fetchUtils';
import MapComponent from '../components/Map';
import PlacesAutocomplete from '../components/PlacesAutocomplete';
import '../styles/PhotoUpload.css';

const PhotoUpload = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [file, setFile] = useState(null);
  const [deviceId, setDeviceId] = useState('');
  const [latitude, setLatitude] = useState(null);
  const [longitude, setLongitude] = useState(null);
  const [devices, setDevices] = useState([]);
  const [message, setMessage] = useState('');
  const [userId, setUserId] = useState(null);
  const [mapCenter, setMapCenter] = useState({ lat: 51.505, lng: -0.09 });

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const userData = await fetchWithToken('/api/users/me');
        setUserId(userData.id);
        const devicesData = await fetchWithToken(`/api/users/${userData.id}/devices`);
        setDevices(devicesData);
      } catch (error) {
        console.error('Failed to fetch user data:', error);
      }
    };
    fetchUserData();
  }, []);

  const handleLocationSelect = ({ lat, lng }) => {
    setLatitude(lat);
    setLongitude(lng);
    setMapCenter({ lat, lng });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('title', title);
    formData.append('description', description);
    formData.append('file', file);
    if (deviceId) formData.append('deviceId', deviceId);
    if (latitude !== null) formData.append('latitude', latitude);
    if (longitude !== null) formData.append('longitude', longitude);

    try {
      const result = await fetchWithToken('/api/photos/upload', {
        method: 'POST',
        body: formData,
      });
      setMessage(result.message || result); 
    } catch (error) {
      setMessage('Upload failed: ' + error.message);
    }
  };

  return (
    <div className="photo-upload-container">
      <h1 className="photo-upload-title">Upload Photo</h1>
      <form onSubmit={handleSubmit} className="photo-upload-form">
        <div>
          <label htmlFor="title">Title:</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="description">Description:</label>
          <textarea
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          ></textarea>
        </div>
        <div>
          <label htmlFor="file">Photo:</label>
          <input
            type="file"
            id="file"
            onChange={(e) => setFile(e.target.files[0])}
            required
          />
        </div>
        {userId && (
          <div>
            <label htmlFor="device">Device (optional):</label>
            <select
              id="device"
              value={deviceId}
              onChange={(e) => setDeviceId(e.target.value)}
            >
              <option value="">Select Device</option>
              {devices.map((device) => (
                <option key={device.id} value={device.id}>
                  {device.type} - {device.model}
                </option>
              ))}
            </select>
          </div>
        )}
        <PlacesAutocomplete onPlaceSelected={handleLocationSelect} />
        <MapComponent onLocationSelect={handleLocationSelect} center={mapCenter} />
        <button type="submit">Upload</button>
      </form>
      {message && <div className="message"><h3>{message}</h3></div>}
    </div>
  );
};

export default PhotoUpload;
