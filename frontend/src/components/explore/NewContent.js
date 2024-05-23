import React, { useState, useEffect } from 'react';
import { Box, Grid, Typography } from '@mui/material';
import fetchWithToken from '../../utils/fetchUtils';
import ContentBox from './ContentBox';
import SeeMoreBox from './SeeMoreBox';
import timeSince from '../../utils/timeCalc'

const NewContent = () => {
  const [newPhotos, setNewPhotos] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);

  const loadNewPhotos = async (pageToLoad) => {
    setLoading(true);
    try {
      const response = await fetchWithToken(`/api/photos/new?page=${pageToLoad}&size=4`);
      setNewPhotos(prevPhotos => [...prevPhotos, ...response.content]);
    } catch (error) {
      console.error('Error fetching new photos:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadNewPhotos(0);
  }, []);

  const handleLoadMore = () => {
    const nextPage = page + 1;
    setPage(nextPage);
    loadNewPhotos(nextPage);
  };

  return (
    <Box sx={{ mb: 5 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>
        New Content
      </Typography>
      <Grid container spacing={2}>
        {newPhotos.map((photo, index) => (
          <Grid item key={index}>
            <ContentBox 
              id={photo.id}
              imageUrl={photo.imageUrl} 
              content={`Uploaded ${timeSince(new Date(photo.uploadedAt))}`} 
            />
          </Grid>
        ))}
        <Grid item>
          <SeeMoreBox onClick={handleLoadMore} loading={loading} />
        </Grid>
      </Grid>
    </Box>
  );
};

export default NewContent;