import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Masonry from 'react-masonry-css';
import '../styles/UserProfile.css';

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
  const navigate = useNavigate();
  const [userProfile, setUserProfile] = useState(null);
  const [photos, setPhotos] = useState([]);
  const [isCurrentUser, setIsCurrentUser] = useState(false);

  useEffect(() => {
    const fetchProfileData = async () => {
      try {
        const userProfileData = await fetchWithToken(`/api/users/${username}`);
        setUserProfile(userProfileData.userProfile);
        setPhotos(userProfileData.photos);
      } catch (error) {
        console.error('Error fetching user profile:', error);
      }
    };

    const fetchCurrentUserProfile = async () => {
      try {
        const currentUserProfileData = await fetchWithToken('/api/users/me');
        setIsCurrentUser(currentUserProfileData.username === username);
      } catch (error) {
        console.error('Error fetching current user profile:', error);
      }
    };

    fetchProfileData();
    fetchCurrentUserProfile();
  }, [username]);

  const handlePhotoClick = (photoId) => {
    navigate(`/photos/${photoId}`);
  };

  if (!userProfile) return <div>Loading...</div>;

  const breakpointColumnsObj = {
    default: 3,
    1100: 3,
    700: 2,
    500: 1,
  };

  return (
    <div className="user-profile">
      <header className="profile-header">
        <div className="profile-info">
          <img
            className="profile-pic"
            src={userProfile.profilePictureUrl || 'default-profile-pic-url.jpg'}
            alt={`${userProfile.username}'s profile`}
          />
          <h2>{userProfile.username}</h2>
          <p className="bio">{userProfile.bio}</p>
          {isCurrentUser && (
            <button onClick={() => navigate('/edit-profile')}>Change Details</button>
          )}
        </div>
      </header>

      <section className="gallery">
        <h2>Photos</h2>
        <Masonry
          breakpointCols={breakpointColumnsObj}
          className="my-masonry-grid"
          columnClassName="my-masonry-grid_column">
          {photos.map((photo, index) => (
            <div key={index} onClick={() => handlePhotoClick(photo.id)}>
              <img src={photo.imageUrl} alt={`Content ${index}`} />
            </div>
          ))}
        </Masonry>
      </section>
    </div>
  );
};

export default UserProfile;
