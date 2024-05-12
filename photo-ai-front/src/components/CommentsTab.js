// src/components/CommentsTab.js

import React, { useState, useEffect } from 'react';
import fetchWithToken from '../utils/fetchUtils';
import '../styles/CommentsTab.css';

const CommentsTab = () => {
    const [comments, setComments] = useState([]);
    const [error, setError] = useState('');

    // Fetch all comments from the backend and sort by commentedAt in descending order
    useEffect(() => {
        const fetchComments = async () => {
            try {
                const data = await fetchWithToken('/api/admin/comments');
                data.sort((a, b) => new Date(b.commentedAt) - new Date(a.commentedAt));
                setComments(data);
            } catch (err) {
                setError('Error fetching comments: ' + err.message);
            }
        };
        fetchComments();
    }, []);

    // Function to handle the deletion of a comment
    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this comment?")) return;
        try {
            await fetchWithToken(`/api/admin/comments/${id}`, { method: 'DELETE' });
            setComments(comments.filter(comment => comment.id !== id));
        } catch (err) {
            setError('Error deleting comment: ' + err.message);
        }
    };

    return (
        <div>
            <h2>Comment Management</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <div className="comments-grid">
                {comments.map((comment) => (
                    <div key={comment.id} className="comment-item">
                        <div className="comment-details">
                            <p>ID: {comment.id}</p>
                            <p>User ID: {comment.userId}</p>
                            <p>Photo ID: {comment.photoId}</p>
                            <p>Content: {comment.comment || 'No Content'}</p>
                            <p>Commented At: {new Date(comment.commentedAt).toLocaleString()}</p>
                            <button onClick={() => handleDelete(comment.id)} className="button delete-button">Delete</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CommentsTab;
