import React, { useState, useEffect } from 'react';
import UsersTab from '../components/UserTab';
import LogsTab from '../components/LogsTab';
import PhotosTab from '../components/PhotosTab';
import Tab from '../components/Tab';
import fetchWithToken from '../utils/fetchUtils';
import '../styles/Admin.css';
import CommentsTab from '../components/admin/CommentsTab';

const Admin = () => {
  const [activeTab, setActiveTab] = useState('users');
  const [user, setUser] = useState(null);
  const [isAuthorized, setIsAuthorized] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchCurrentUser = async () => {
      try {
        const data = await fetchWithToken('/api/users/me');
        if (data.role !== 'ROLE_ADMIN') {
          setIsAuthorized(false);
        } else {
          setUser(data);
          setIsAuthorized(true);
        }
      } catch (error) {
        console.error('Failed to fetch user data:', error);
        setIsAuthorized(false);
      } finally {
        setIsLoading(false);
      }
    };
    fetchCurrentUser();
  }, []);

  if (isLoading) {
    return <div className="admin-header">Loading...</div>;
  }

  if (!isAuthorized) {
    return <div className="admin-header">No authorization!</div>;
  }

  return (
    <div className="admin-container">
      <div className="admin-header">Admin Panel</div>
      <div className="logged-in-as">Logged in as {user?.username || 'loading...'}</div>
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
