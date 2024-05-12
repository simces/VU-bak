import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import PhotoForm from './PhotoForm';
import fetchWithToken from '../utils/fetchUtils';
import '../styles/PhotosTab.css';

const PhotosTab = () => {
    const [photos, setPhotos] = useState([]);
    const [selectedPhoto, setSelectedPhoto] = useState(null);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isViewModalOpen, setIsViewModalOpen] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchPhotos = async () => {
            try {
                const data = await fetchWithToken('/api/admin/photos');
                data.sort((a, b) => new Date(b.uploadedAt) - new Date(a.uploadedAt));
                setPhotos(data);
            } catch (err) {
                setError('Error fetching photos: ' + err.message);
            }
        };
        fetchPhotos();
    }, []);

    const openViewModal = (photo) => {
        setSelectedPhoto(photo);
        setIsViewModalOpen(true);
    };

    const openEditModal = (photo) => {
        setSelectedPhoto(photo);
        setIsEditModalOpen(true);
    };

    const closeViewModal = () => setIsViewModalOpen(false);

    const closeEditModal = () => setIsEditModalOpen(false);

    const handleUpdatePhoto = async (updatedPhoto) => {
        try {
            const response = await fetchWithToken(`/api/admin/photos/${updatedPhoto.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedPhoto)
            });
            if (response.ok) {
                setPhotos(photos.map(photo => photo.id === updatedPhoto.id ? updatedPhoto : photo));
                closeEditModal();
            } else {
                throw new Error('Failed to update photo');
            }
        } catch (err) {
            setError('Error updating photo: ' + err.message);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this photo?")) return;
        try {
            await fetchWithToken(`/api/admin/photos/${id}`, { method: 'DELETE' });
            setPhotos(photos.filter(photo => photo.id !== id));
        } catch (err) {
            setError('Error deleting photo: ' + err.message);
        }
    };

    return (
        <div>
            <h2>Photo Management</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <div className="photos-grid">
                {photos.map((photo) => (
                    <div key={photo.id} className="photo-item">
                        <img src={photo.imageUrl} alt={photo.title} className="photo-image" onClick={() => openViewModal(photo)}/>
                        <div className="photo-details">
                            <p>ID: {photo.id}</p>
                            <p>User ID: {photo.userId}</p>
                            <p>Title: {photo.title || 'No Title'}</p>
                            <p>Description: {photo.description || 'No Description'}</p>
                            <p>Uploaded At: {new Date(photo.uploadedAt).toLocaleString()}</p>
                            <button onClick={() => openEditModal(photo)} className="button">Edit</button>
                            <button onClick={() => handleDelete(photo.id)} className="button delete-button">Delete</button>
                        </div>
                    </div>
                ))}
            </div>
            {selectedPhoto && (
                <>
                    <Modal
                        isOpen={isViewModalOpen}
                        onRequestClose={closeViewModal}
                        contentLabel="View Photo Modal"
                        className="view-modal"
                        overlayClassName="view-overlay"
                    >
                        <img src={selectedPhoto.imageUrl} alt={selectedPhoto.title} className="modal-photo" onClick={closeViewModal} />
                    </Modal>

                    <Modal
                        isOpen={isEditModalOpen}
                        onRequestClose={closeEditModal}
                        contentLabel="Edit Photo Modal"
                        className="edit-modal"
                        overlayClassName="edit-overlay"
                    >
                        <PhotoForm
                            photo={selectedPhoto}
                            onSubmit={handleUpdatePhoto}
                            onCancel={closeEditModal}
                        />
                    </Modal>
                </>
            )}
        </div>
    );
};

export default PhotosTab;