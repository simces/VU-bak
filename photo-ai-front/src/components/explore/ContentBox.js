import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, CardContent, Typography, Box } from '@mui/material';

const ContentBox = ({ content, imageUrl, likeCount, likesAndComments, id }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    if (id !== undefined) {
      navigate(`/photos/${id}`);
    } else {
      console.error('No ID provided for ContentBox');
    }
  };

  return (
    <Card 
      sx={{ width: 250, height: 150, m: 1, borderRadius: 2, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
      onClick={handleClick}
    >
      {imageUrl ? (
        <Box sx={{ height: '100%', position: 'relative' }}>
          <img src={imageUrl} alt="content" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
          <Box sx={{ position: 'absolute', bottom: 0, left: 0, width: '100%', bgcolor: 'rgba(0, 0, 0, 0.5)', color: 'white', p: 1 }}>
            {likeCount !== undefined && <Typography variant="body2">{likeCount} likes</Typography>}
            {likesAndComments && <Typography variant="body2">{likesAndComments}</Typography>}
            {content && <Typography variant="body2">{content}</Typography>}
          </Box>
        </Box>
      ) : (
        <CardContent>
          <Typography variant="body2" color="text.secondary">
            {content}
          </Typography>
        </CardContent>
      )}
    </Card>
  );
};

export default ContentBox;
