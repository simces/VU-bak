import React, { useState, useEffect } from 'react';
import fetchWithToken from '../utils/fetchUtils';

const ChangeDetails = () => {
  const [userDetails, setUserDetails] = useState({
    username: '',
    email: '',
    bio: '',
    profilePictureUrl: '',
  });

  const [password, setPassword] = useState({
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: '',
  });

  useEffect(() => {
    const fetchCurrentUserDetails = async () => {
      try {
        const data = await fetchWithToken('/api/users/me');
        setUserDetails({
          username: data.username,
          email: data.email,
          bio: data.bio,
          profilePictureUrl: data.profilePictureUrl,
        });
      } catch (error) {
        console.error('Failed to fetch user details:', error);
      }
    };

    fetchCurrentUserDetails();
  }, []);

  const handleUserDetailsChange = (e) => {
    const { name, value } = e.target;
    setUserDetails((prevDetails) => ({ ...prevDetails, [name]: value }));
  };

  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPassword((prevPassword) => ({ ...prevPassword, [name]: value }));
  };

  const submitUserDetails = async (e) => {
    e.preventDefault();
    try {
      await fetchWithToken('/api/users/update-profile', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userDetails),
      });
      alert('Profile updated successfully!');
    } catch (error) {
      alert('Failed to update profile: ' + error.message);
    }
  };

  const submitPasswordChange = async (e) => {
    e.preventDefault();
    if (password.newPassword !== password.confirmNewPassword) {
      alert('New password and confirmation do not match.');
      return;
    }
    try {
      await fetchWithToken('/api/users/change-password', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          currentPassword: password.currentPassword,
          newPassword: password.newPassword,
        }),
      });
      alert('Password changed successfully!');
    } catch (error) {
      alert('Failed to change password: ' + error.message);
    }
  };

  return (
    <div>
      <h2>Change User Details</h2>
      <form onSubmit={submitUserDetails}>
        {/* profile pic change */}
        <input type="text" name="username" placeholder="Username" value={userDetails.username} onChange={handleUserDetailsChange} />
        <input type="email" name="email" placeholder="Email" value={userDetails.email} onChange={handleUserDetailsChange} />
        <textarea name="bio" placeholder="Bio" value={userDetails.bio} onChange={handleUserDetailsChange} />
        <button type="submit">Update Profile</button>
      </form>

      <h2>Change Password</h2>
      <form onSubmit={submitPasswordChange}>
        <input type="password" name="currentPassword" placeholder="Current Password" value={password.currentPassword} onChange={handlePasswordChange} />
        <input type="password" name="newPassword" placeholder="New Password" value={password.newPassword} onChange={handlePasswordChange} />
        <input type="password" name="confirmNewPassword" placeholder="Confirm New Password" value={password.confirmNewPassword} onChange={handlePasswordChange} />
        <button type="submit">Change Password</button>
      </form>
    </div>
  );
};

export default ChangeDetails;
