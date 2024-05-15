import React, { useState, useEffect } from 'react';
import fetchWithToken from '../utils/fetchUtils';
import '../styles/PhotoUpload.css';

const PhotoUpload = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [file, setFile] = useState(null);
  const [deviceId, setDeviceId] = useState('');
  const [devices, setDevices] = useState([]);
  const [message, setMessage] = useState('');
  const [userId, setUserId] = useState(null);

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('title', title);
    formData.append('description', description);
    formData.append('file', file);
    if (deviceId) {
      formData.append('deviceId', deviceId);
    }

    console.log('Submitting form with deviceId:', deviceId);

    try {
      const result = await fetchWithToken('http://localhost:8080/api/photos/upload', {
        method: 'POST',
        body: formData,
      });
      setMessage(result.message || result); // Display the message from JSON or text directly
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
        <button type="submit">Upload</button>
      </form>
      {message && <div className="message"><h3>{message}</h3></div>}
    </div>
  );
};

export default PhotoUpload;
