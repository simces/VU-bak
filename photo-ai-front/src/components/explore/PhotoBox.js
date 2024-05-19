import React from 'react';
import { Card, CardMedia } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const PhotoBox = ({ imageUrl, photoId }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/photos/${photoId}`);
  };

  return (
    <Card 
      sx={{ Width: 250, Height: 150, m: 1, borderRadius: 2, cursor: 'pointer' }}
      onClick={handleClick}
    >
      <CardMedia
        component="img"
        image={imageUrl}
        alt="Photo"
        sx={{ height: '150px', width: '250px', objectFit: 'cover', borderRadius: 2 }}
      />
    </Card>
  );
};

export default PhotoBox;
