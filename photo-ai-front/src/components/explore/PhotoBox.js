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
      sx={{ minWidth: 150, minHeight: 200, m: 1, borderRadius: 2, cursor: 'pointer' }}
      onClick={handleClick}
    >
      <CardMedia
        component="img"
        image={imageUrl}
        alt="Photo"
        sx={{ height: '200px', width: '150px', objectFit: 'cover', borderRadius: 2 }}
      />
    </Card>
  );
};

export default PhotoBox;
