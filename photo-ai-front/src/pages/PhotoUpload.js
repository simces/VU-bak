import React, { useState } from 'react';
import fetchWithToken from '../utils/fetchUtils';


const PhotoUpload = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');

const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('title', title);
    formData.append('description', description);
    formData.append('file', file);
  
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
    <div>
      <h1>Upload Photo</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="title">Title:</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="description">Description:</label>
          <textarea
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          ></textarea>
        </div>
        <div>
          <label htmlFor="file">Photo:</label>
          <input
            type="file"
            id="file"
            onChange={(e) => setFile(e.target.files[0])}
          />
        </div>
        <button type="submit">Upload</button>
      </form>
      {message && <div><h3>{message}</h3></div>}
    </div>
  );
};

export default PhotoUpload;
