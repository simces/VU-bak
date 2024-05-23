import React, { useState, useEffect } from 'react';
import { Box, Grid, Typography } from '@mui/material';
import fetchWithToken from '../../utils/fetchUtils';
import ContentBox from './ContentBox';
import SeeMoreBox from './SeeMoreBox';

const TopContent = () => {
  const [topPhotos, setTopPhotos] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);

  const loadTopPhotos = async (pageToLoad) => {
    setLoading(true);
    try {
      const response = await fetchWithToken(`/api/photos/top?page=${pageToLoad}&size=4`);
      setTopPhotos(prevPhotos => [...prevPhotos, ...response.content]);
    } catch (error) {
      console.error('Error fetching top photos:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTopPhotos(0);
  }, []);

  const handleLoadMore = () => {
    const nextPage = page + 1;
    setPage(nextPage);
    loadTopPhotos(nextPage);
  };

  return (
    <Box sx={{ mb: 5 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>
        Top Content
      </Typography>
      <Grid container spacing={2}>
        {topPhotos.map((photo, index) => (
          <Grid item key={index}>
            <ContentBox 
              id={photo.id}
              imageUrl={photo.imageUrl} 
              likesAndComments={`${photo.likeCount} likes, ${photo.commentCount} comments`} 
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

export default TopContent;
