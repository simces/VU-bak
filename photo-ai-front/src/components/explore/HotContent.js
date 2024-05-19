import React, { useState, useEffect } from 'react';
import { Box, Grid, Typography } from '@mui/material';
import fetchWithToken from '../../utils/fetchUtils';
import ContentBox from './ContentBox';
import SeeMoreBox from './SeeMoreBox';

const HotContent = () => {
  const [hotPhotos, setHotPhotos] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);

  const loadHotPhotos = async (pageToLoad) => {
    setLoading(true);
    try {
      const response = await fetchWithToken(`/api/photos/hot?page=${pageToLoad}&size=4`);
      setHotPhotos(prevPhotos => [...prevPhotos, ...response]);
    } catch (error) {
      console.error('Error fetching hot photos:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHotPhotos(0);
  }, []);

  const handleLoadMore = () => {
    const nextPage = page + 1;
    setPage(nextPage);
    loadHotPhotos(nextPage);
  };

  return (
    <Box sx={{ mb: 5 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>
        Hot Content
      </Typography>
      <Grid container spacing={2}>
        {hotPhotos.map((photo, index) => (
          <Grid item key={index}>
            <ContentBox imageUrl={photo.imageUrl} likeCount={photo.likeCount} />
          </Grid>
        ))}
        <Grid item>
          <SeeMoreBox onClick={handleLoadMore} loading={loading} />
        </Grid>
      </Grid>
    </Box>
  );
};

export default HotContent;