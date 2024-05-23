import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import fetchWithToken from '../../utils/fetchUtils';
import UserForm from './UserForm';
import '../../styles/Admin.css';
import '../../styles/UserTab.css';

Modal.setAppElement('#root');

const UsersTab = () => {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [error, setError] = useState('');
    const [expandedBio, setExpandedBio] = useState({});

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const data = await fetchWithToken('/api/admin/users'); 
                setUsers(data.sort((a, b) => a.id - b.id));
            } catch (err) {
                setError('Error fetching users: ' + err.message);
            }
        };
        fetchUsers();
    }, []);

    const toggleBio = (id) => {
        setExpandedBio(prev => ({ ...prev, [id]: !prev[id] }));
    };

    const renderBio = (user) => {
        if (!user.bio) {
            return <span className="no-bio"> No bio available </span>;
        }
        return expandedBio[user.id] ? user.bio : `${user.bio.substring(0, 100)}...`;
    };

    const handleEditClick = (user) => {
        setSelectedUser(user);
        setIsModalOpen(true);
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this user?")) {
            return; // Cancel deletion 
        }
        try {
            await fetchWithToken(`/api/admin/users/${id}`, { method: 'DELETE' });
            const updatedUsers = users.filter(user => user.id !== id);
            setUsers(updatedUsers);
        } catch (err) {
            setError('Error deleting user: ' + err.message);
        }
    };

    const handleUpdateUsers = (updatedUser) => {
        const newUsers = users.map(user => user.id === updatedUser.id ? updatedUser : user);
        setUsers(newUsers);
        setIsModalOpen(false);
    };

    const closeModal = () => {
        setSelectedUser(null);
        setIsModalOpen(false);
    };

    const formatDate = (dateString) => {
        const options = {
            year: 'numeric', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit', hour12: true
        };
        return new Date(dateString).toLocaleString('en-US', options);
    };

    return (
        <div>
            <h2>Users Management</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <table className="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Profile Picture</th>
                        <th>Bio</th>
                        <th>Role</th>
                        <th>Created At</th>
                        <th>Updated At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.username}</td>
                            <td>{user.email}</td>
                            <td><img src={user.profilePictureUrl} alt="profile" className="profile-picture" /></td>
                            <td onClick={() => toggleBio(user.id)} className={`bio-content ${expandedBio[user.id] ? 'expanded' : ''}`}>
                                {renderBio(user)}
                            </td>
                            <td>{user.role}</td>
                            <td>{formatDate(user.createdAt)}</td>
                            <td>{formatDate(user.updatedAt)}</td>
                            <td>
                                <button onClick={() => handleEditClick(user)} className="button">Edit</button>
                                <button onClick={() => handleDelete(user.id)} className="button delete-button">Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <Modal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
            >
                <UserForm
                    user={selectedUser}
                    onFormSubmit={handleUpdateUsers}
                    onCancel={closeModal}
                />
            </Modal>
        </div>
    );
};

export default UsersTab;


