import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../styles/styles.css';
import Masonry from 'react-masonry-css';

const fetchWithToken = async (url) => {
  const token = localStorage.getItem('token');
  const headers = {
    Authorization: `Bearer ${token}`,
  };
  const response = await fetch(url, { headers });
  if (!response.ok) throw new Error('Network response was not ok');
  return response.json();
};

const UserProfile = () => {
  const { username } = useParams(); 
  const [userProfile, setUserProfile] = useState(null);
  const [photos, setPhotos] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userProfileData = await fetchWithToken(`/api/users/${username}`);
        setUserProfile(userProfileData.userProfile);
        setPhotos(userProfileData.photos);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [username]);

  if (!userProfile) return <div>Loading...</div>;

  const breakpointColumnsObj = {
    default: 3,
    1100: 3,
    700: 2,
    500: 1
  };

  return (
<div className="user-profile">
  <header className="profile-header">
    <div className="profile-info">
    <img className="profile-pic" src={userProfile.profilePictureUrl || 'default-profile-pic-url.jpg'} alt={`${userProfile.username}'s profile`} />
      <h2>{userProfile.username}</h2>
      <p className="bio">{userProfile.bio}</p>
    </div>
  </header>

  <section className="gallery">
        <h2>Photos</h2>
        <Masonry
          breakpointCols={breakpointColumnsObj}
          className="my-masonry-grid"
          columnClassName="my-masonry-grid_column">
          {photos.map((photo, index) => (
            <div key={index}>
              <img src={photo.imageUrl} alt={`Content ${index}`} />
            </div>
          ))}
        </Masonry>
      </section>
    </div>

  );
};

export default UserProfile;