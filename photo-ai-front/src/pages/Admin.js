import React, { useState, useEffect } from 'react';
import UsersTab from '../components/UserTab';
import LogsTab from '../components/LogsTab';
import PhotosTab from '../components/PhotosTab';
import Tab from '../components/Tab';
import fetchWithToken from '../utils/fetchUtils'; 
import '../styles/Admin.css';
import CommentsTab from '../components/CommentsTab';

const Admin = () => {
  const [activeTab, setActiveTab] = useState('users');
  const [username, setUsername] = useState('');
  const [isAuthorized, setIsAuthorized] = useState(true);

  useEffect(() => {
      const fetchCurrentUser = async () => {
          try {
              const data = await fetchWithToken('/api/users/me');
              setUsername(data.username);
          } catch (error) {
              console.error('Failed to fetch user data:', error);
              setIsAuthorized(false);
          }
      };
      fetchCurrentUser();
  }, []);

  if (!isAuthorized) {
      return <div className="admin-header">No authorization!</div>;
  }

  return (
    <div className="admin-container">
      <div className="admin-header">Admin Panel</div>
      <div className="logged-in-as">Logged in as {username || 'loading...'}</div>
      <div className="tab-list">
        <Tab label="Users" isActive={activeTab === 'users'} onClick={() => setActiveTab('users')} />
        <Tab label="Photos" isActive={activeTab === 'photos'} onClick={() => setActiveTab('photos')} />
        <Tab label="Comments" isActive={activeTab === 'comments'} onClick={() => setActiveTab('comments')} />
        <Tab label="Logs" isActive={activeTab === 'logs'} onClick={() => setActiveTab('logs')} />
      </div>
      <div className="tab-content">
        {activeTab === 'users' && <UsersTab />}
        {activeTab === 'logs' && <LogsTab />}
        {activeTab === 'photos' && <PhotosTab />}
        {activeTab === 'comments' && <CommentsTab />}
      </div>
    </div>
  );
};

export default Admin;