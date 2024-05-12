import React, { useEffect, useState } from 'react';
import fetchWithToken from '../utils/fetchUtils';
import '../styles/Feed.css';
import timeSince from '../utils/timeCalc';


const Feed = () => {
    const [feedData, setFeedData] = useState({ content: [] });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
  
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
  
    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error fetching feed: {error}</p>;
  
    return (
      <div className="feed">
        {feedData.content.map(({ photoDTO, userProfileDTO, tagDTO }) => (
          <div key={photoDTO.id} className="feedItem">
            <div className="userInfo">
              <img src={userProfileDTO.profilePictureUrl} alt={userProfileDTO.username} className="userImage" />
              <p className="username">{userProfileDTO.username}</p>
            </div>
            <p className="tag">#{tagDTO.name}</p>
            <img src={photoDTO.imageUrl} alt={photoDTO.title} className="photoImage" />
            <div className="photoInfo">
              <p className="title">{photoDTO.title}</p>
              <p className="description">{photoDTO.description}</p>
              <p className="uploadedAt">Uploaded: {timeSince(photoDTO.uploadedAt)}</p>
            </div>
          </div>
        ))}
      </div>
    );
  };
  
  export default Feed;
