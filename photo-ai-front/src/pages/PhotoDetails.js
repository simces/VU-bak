import React, { useState, useEffect, useCallback } from 'react';
import fetchWithToken from '../utils/fetchUtils';
import { useParams, Link } from 'react-router-dom';

const PhotoDetails = () => {
    const { photoId } = useParams();
    const [photoDetails, setPhotoDetails] = useState(null);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [userLike, setUserLike] = useState(null);
    const [likeCount, setLikeCount] = useState(0);
    const [likers, setLikers] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const refreshLikeStatusAndDetails = useCallback(async () => {
        try {
            const likeDetails = await fetchWithToken(`/api/photos/${photoId}/likelist`);
            setLikeCount(likeDetails.likeCount);
            setLikers(likeDetails.usernames || []);

            const likeStatus = await fetchWithToken(`/api/photos/test/${photoId}`);
            if (likeStatus.liked) {
                setUserLike(likeStatus);
            } else {
                setUserLike(null);
            }
        } catch (error) {
            console.error('Error refreshing like status and details:', error.message);
        }
    }, [photoId]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const photoDetailsData = await fetchWithToken(`/api/photos/${photoId}`);
                setPhotoDetails(photoDetailsData);

                const commentsData = await fetchWithToken(`/api/photos/${photoId}/comments`);
                setComments(commentsData);

                await refreshLikeStatusAndDetails();
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, [photoId, refreshLikeStatusAndDetails]);

    const toggleModal = () => {
        setIsModalOpen(!isModalOpen);
    };

    const handleCommentSubmit = async (event) => {
        event.preventDefault();
        const commentData = JSON.stringify({
            comment: newComment 
        });

        try {
            const newCommentData = await fetchWithToken(`/api/photos/${photoId}/comments`, {
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

    const handleLike = async () => {
        try {
            await fetchWithToken(`/api/photos/${photoId}/likes`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });
            await refreshLikeStatusAndDetails(); 
        } catch (error) {
            console.error('Error liking the photo:', error.message);
        }
    };

    const handleUnlike = async () => {
        if (!userLike || !userLike.likeId) return;
        try {
            await fetchWithToken(`/api/photos/likes/${userLike.likeId}`, { method: 'DELETE' });
            await refreshLikeStatusAndDetails();
        } catch (error) {
            console.error('Error unliking the photo:', error.message);
        }
    };

    if (!photoDetails) return <div>Loading...</div>;

    return (
        <div>
            <h2>{photoDetails.photo.title}</h2>
            <img src={photoDetails.photo.imageUrl} alt={photoDetails.photo.title} style={{ maxWidth: '100%' }} />
            <p>Description: {photoDetails.photo.description}</p>
            <p>Tag: {photoDetails.photo.tags.map(tag => `#${tag.name}`).join(', ')}</p>

            <p>Uploaded by: 
                <img src={photoDetails.user.profilePictureUrl} alt={photoDetails.user.username} style={{ width: '50px', height: '50px', borderRadius: '50%', marginRight: '10px' }} />
                {photoDetails.user.username}
            </p>
            <p>Uploaded at: {new Date(photoDetails.photo.uploadedAt).toLocaleString()}</p>

            <div>
                {userLike ? (
                    <button onClick={handleUnlike}>Unlike</button>
                ) : (
                    <button onClick={handleLike}>Like</button>
                )}
                {likeCount === 1 ? (
                    <span><Link to={`/users/${likers[0]}`} style={{ color: 'blue' }}>{likers[0]}</Link> has liked this photo</span>
                ) : likeCount === 2 ? (
                    <span>
                        <Link to={`/users/${likers[0]}`} style={{ color: 'blue' }}>{likers[0]}</Link> and 
                        <Link to={`/users/${likers[1]}`} style={{ color: 'blue' }}>{likers[1]}</Link> have liked this photo
                    </span>
                ) : likeCount > 2 ? (
                    <>
                        <span>
                            <Link to={`/users/${likers[0]}`} style={{ color: 'blue' }}>{likers[0]}</Link> and 
                            <button onClick={toggleModal} style={{ background: 'none', color: 'blue', border: 'none', padding: 0, cursor: 'pointer' }}>
                                {likeCount - 1} others
                            </button>
                             have liked this photo
                        </span>

                        {isModalOpen && (
                            <div style={{ position: 'fixed', top: '20%', left: '30%', background: 'white', padding: '20px', zIndex: 1000 }}>
                                <h2>Likers</h2>
                                <div style={{ overflowY: 'scroll', maxHeight: '300px' }}>
                                    {likers.slice(0, 50).map((liker, index) => (
                                        <p key={index}><Link to={`/users/${liker}`} style={{ color: 'blue' }}>{liker}</Link></p>
                                    ))}
                                </div>
                                <button onClick={toggleModal}>Close</button>
                            </div>
                        )}
                    </>
                ) : (
                    <span>No likes yet</span>
                )}
            </div>

            <div>
                <h3>Comments:</h3>
                {comments.length > 0 ? comments.map((comment) => (
                <div key={comment.id}>
                    <p>{comment.comment}</p>
                    <p>By: {comment.username || 'Unknown user'} at {new Date(comment.commentedAt).toLocaleString()}</p>
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
