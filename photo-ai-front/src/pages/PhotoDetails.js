import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import fetchWithToken from '../utils/fetchUtils';

const PhotoDetails = () => {
    const { photoId } = useParams();
    const [photoDetails, setPhotoDetails] = useState(null);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try { 
                const photoDetailsData = await fetchWithToken(`/api/photos/${photoId}`);
                setPhotoDetails(photoDetailsData);
                
                const commentsData = await fetchWithToken(`/api/photos/${photoId}/comments`);
                setComments(commentsData);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, [photoId]);

    const handleCommentSubmit = async (event) => {
        event.preventDefault();
        const commentData = JSON.stringify({
            comment: newComment // Removed userId from the payload
        });
    
        try {
            const newCommentData = await fetchWithToken(`http://localhost:8080/api/photos/${photoId}/comments`, {
                method: 'POST',
                body: commentData,
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            setNewComment('');
            setComments([...comments, newCommentData]);
        } catch (error) {
            console.error('Error posting comment:', error.message);
        }
    };
    

    if (!photoDetails) return <div>Loading...</div>;

    return (
        <div>
            <h2>{photoDetails.photoDTO.title}</h2>
            <img src={photoDetails.photoDTO.imageUrl} alt={photoDetails.photoDTO.title} style={{ maxWidth: '100%' }} />
            <p>Description: {photoDetails.photoDTO.description}</p>
            <p>Tag: {photoDetails.tagDTO.name}</p>
            
            <p>Uploaded by: 
                <img src={photoDetails.userProfileDTO.profilePictureUrl} alt={photoDetails.userProfileDTO.username} style={{ width: '50px', height: '50px', borderRadius: '50%', marginRight: '10px' }} />
                {photoDetails.userProfileDTO.username}
            </p>
            <p>Uploaded at: {new Date(photoDetails.photoDTO.uploadedAt).toLocaleString()}</p>

            <div>
                <h3>Comments:</h3>
                {comments.length > 0 ? comments.map((comment) => (
                <div key={comment.id}>
                    <p>{comment.comment}</p>
                    <p>By: {comment.user?.username || 'Unknown user'} at {comment.commentedAt ? new Date(comment.commentedAt).toLocaleString() : 'Unknown time'}</p>
                </div>
                )) : <p>No comments yet.</p>}
            </div>

            <form onSubmit={handleCommentSubmit}>
                <input 
                    type="text" 
                    value={newComment} 
                    onChange={(e) => setNewComment(e.target.value)} 
                    placeholder="Write a comment..." 
                />
                <button type="submit">Post Comment</button>
            </form>
        </div>
    );
};

export default PhotoDetails;
