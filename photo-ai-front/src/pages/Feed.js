import React, { useEffect, useState } from 'react';
import fetchWithToken from '../utils/fetchUtils';
import '../styles/Feed.css';
import timeSince from '../utils/timeCalc';
import LocationMapModal from '../components/LocationMapModal';

const Feed = () => {
  const [feedData, setFeedData] = useState({ content: [] });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [isMapModalOpen, setIsMapModalOpen] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState({ latitude: null, longitude: null, location: '' });

  useEffect(() => {
    const fetchFeedData = async () => {
      setLoading(true);
      try {
        const data = await fetchWithToken('/api/feed');
        setFeedData(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchFeedData();
  }, []);

  const toggleMapModal = (latitude, longitude, location) => {
    setSelectedLocation({ latitude, longitude, location });
    setIsMapModalOpen(!isMapModalOpen);
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error fetching feed: {error}</p>;

  return (
    <div className="feed-container">
      <div className="feed">
        {feedData.content.map(({ photo, user }) => (
          <div key={photo.id} className="feedItem">
            <div className="userInfo">
              <img src={user.profilePictureUrl} alt={user.username} className="userImage" />
              <p className="username">{user.username}</p>
            </div>
            {photo.tags && photo.tags.length > 0 && (
              <p className="tag">#{photo.tags[0].name}</p>
            )}
            <img src={photo.imageUrl} alt={photo.title} className="photoImage" />
            <div className="photoInfo">
              <p className="title">{photo.title}</p>
              <p className="description">{photo.description}</p>
              <p className="uploadedAt">Uploaded: {timeSince(photo.uploadedAt)}</p>
              {photo.device && (
                <p className="deviceInfo">Taken with: {photo.device.type} - {photo.device.model}</p>
              )}
              {photo.location && (
                <p className="location">
                  Location: <button onClick={() => toggleMapModal(photo.latitude, photo.longitude, photo.location)} style={{ background: 'none', color: 'blue', border: 'none', padding: 0, cursor: 'pointer' }}>
                    {photo.location}
                  </button>
                </p>
              )}
            </div>
          </div>
        ))}
      </div>
      <LocationMapModal
        isOpen={isMapModalOpen}
        toggleModal={() => setIsMapModalOpen(false)}
        latitude={selectedLocation.latitude}
        longitude={selectedLocation.longitude}
        location={selectedLocation.location}
      />
    </div>
  );
};

export default Feed;
