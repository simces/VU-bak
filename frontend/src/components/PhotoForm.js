import React, { useState } from 'react';
import '../styles/PhotoForm.css';

const PhotoForm = ({ photo, onSubmit, onCancel }) => {
    const [title, setTitle] = useState(photo.title);
    const [description, setDescription] = useState(photo.description);

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({ ...photo, title, description });
    };

    return (
        <form onSubmit={handleSubmit} className="photo-edit-form">
            <img src={photo.imageUrl} alt={photo.title} className="form-photo-image"/>
            <label>
                Title:
                <input type="text" value={title} onChange={e => setTitle(e.target.value)} />
            </label>
            <label>
                Description:
                <textarea value={description} onChange={e => setDescription(e.target.value)} />
            </label>
            <div className="form-actions">
                <button type="button" onClick={onCancel} className="button">Cancel</button>
                <button type="submit" className="button">Save Changes</button>
            </div>
        </form>
    );
};

export default PhotoForm;
