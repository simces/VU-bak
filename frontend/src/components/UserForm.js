import React, { useState, useEffect } from 'react';
import fetchWithToken from '../utils/fetchUtils';
import '../styles/UserForm.css';
import Modal from 'react-modal';

const UserForm = ({ user, onFormSubmit, onCancel }) => {
    const [formData, setFormData] = useState({
      username: '',
      email: '',
      profilePictureUrl: '',
      bio: '',
      role: '',
    });
  
    useEffect(() => {
      if (user) {
        setFormData({
          username: user.username,
          email: user.email,
          profilePictureUrl: user.profilePictureUrl,
          bio: user.bio,
          role: user.role,
        });
      }
    }, [user]);
  
    const handleChange = (event) => {
      const { name, value } = event.target;
      setFormData(prev => ({ ...prev, [name]: value }));
    };
  
    const handleSubmit = async (event) => {
      event.preventDefault();
      try {
        const updatedUser = await fetchWithToken(`/api/admin/${user.id}`, {
          method: 'PUT',
          body: JSON.stringify(formData),
          headers: {
            'Content-Type': 'application/json',
          },
        });
        onFormSubmit(updatedUser); 
        onCancel(); 
      } catch (error) {
        console.error('Failed to update user:', error);
      }
    };
  
    const customStyles = {
      content: {
        top: '50%',
        left: '50%',
        right: 'auto',
        bottom: 'auto',
        marginRight: '-50%',
        transform: 'translate(-50%, -50%)',
        width: '80%',
        maxWidth: '600px', 
        background: '#fff',
        borderRadius: '10px',
        padding: '20px'
      },
      overlay: {
        backgroundColor: 'rgba(0, 0, 0, 0.75)' 
      }
    };
  
    return (
      <Modal
        isOpen={true}
        onRequestClose={onCancel}
        style={customStyles} 
      >
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Username:</label>
            <input name="username" value={formData.username} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Email:</label>
            <input type="email" name="email" value={formData.email} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Profile Picture URL:</label>
            <input type="text" name="profilePictureUrl" value={formData.profilePictureUrl} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Bio:</label>
            <textarea name="bio" value={formData.bio} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Role:</label>
            <input name="role" value={formData.role} onChange={handleChange} />
          </div>
          <div style={{ marginTop: '20px' }}>
            <button type="button" onClick={onCancel}>Cancel</button>
            <button type="submit">Save Changes</button>
          </div>
        </form>
      </Modal>
    );
  };
  
  export default UserForm;