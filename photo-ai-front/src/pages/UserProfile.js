import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Masonry from 'react-masonry-css';
import fetchWithToken from '../utils/fetchUtils';
import '../styles/UserProfile.css';

const UserProfile = () => {
  const { username } = useParams();
  const navigate = useNavigate();
  const [userProfile, setUserProfile] = useState(null);
  const [photos, setPhotos] = useState([]);
  const [isCurrentUser, setIsCurrentUser] = useState(false);
  const [isFollowing, setIsFollowing] = useState(false);
  const [followId, setFollowId] = useState(null);
  const [followersCount, setFollowersCount] = useState(0);
  const [followingsCount, setFollowingsCount] = useState(0);

  const fetchFollowCounts = async (userId) => {
    try {
      const followersResponse = await fetchWithToken(`/api/follows/followers/count/${userId}`);
      const followingsResponse = await fetchWithToken(`/api/follows/following/count/${userId}`);
      setFollowersCount(followersResponse);
      setFollowingsCount(followingsResponse);
    } catch (error) {
      console.error('Error fetching follow counts:', error);
    }
  };

  useEffect(() => {
    const fetchProfileData = async () => {
      try {
        const userProfileResponse = await fetchWithToken(`/api/users/${username}`);
        setUserProfile(userProfileResponse.userProfile);
        setPhotos(userProfileResponse.photos);

        const currentUserProfileResponse = await fetchWithToken('/api/users/me');
        setIsCurrentUser(currentUserProfileResponse.username === username);

        if (userProfileResponse.userProfile && userProfileResponse.userProfile.id) {
          fetchFollowCounts(userProfileResponse.userProfile.id);
          const followStatusResponse = await fetchWithToken(`/api/follows/isFollowing/${userProfileResponse.userProfile.id}`);
          if (followStatusResponse.following) {
            setIsFollowing(true);
            setFollowId(followStatusResponse.followId);
          } else {
            setIsFollowing(false);
            setFollowId(null);
          }
        }
      } catch (error) {
        console.error('Error fetching user profile:', error);
      }
    };

    fetchProfileData();
  }, [username]);

  const handleFollow = async () => {
    try {
      const followRequestDTO = { followingId: userProfile.id };
      const followResponseDTO = await fetchWithToken(`/api/follows/follow`, {
        method: 'POST',
        body: JSON.stringify(followRequestDTO),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      setIsFollowing(true);
      setFollowId(followResponseDTO.followId); // Ensure correct field name
      fetchFollowCounts(userProfile.id);
    } catch (error) {
      console.error('Error following user:', error);
    }
  };

  const handleUnfollow = async () => {
    if (!followId) return;

    try {
      await fetchWithToken(`/api/follows/unfollow/${followId}`, { method: 'DELETE' });
      setIsFollowing(false);
      setFollowId(null);
      fetchFollowCounts(userProfile.id);
    } catch (error) {
      console.error('Error unfollowing user:', error);
    }
  };

  const handlePhotoClick = (photoId) => {
    navigate(`/photos/${photoId}`);
  };

  const editProfile = () => {
    navigate('/edit-profile');
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
          <div className="social-counts">
            <span>{followersCount} Followers</span> • <span>{followingsCount} Following</span>
          </div>
          {isCurrentUser ? (
            <button onClick={editProfile}>Edit Profile</button>
          ) : (
            isFollowing ? (
              <button onClick={handleUnfollow}>Unfollow</button>
            ) : (
              <button onClick={handleFollow}>Follow</button>
            )
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
