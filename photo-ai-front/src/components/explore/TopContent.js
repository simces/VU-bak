import React from 'react';
import { Box, Grid, Typography } from '@mui/material';
import ContentBox from './ContentBox';
import SeeMoreBox from './SeeMoreBox';

const TopContent = () => (
  <Box sx={{ mb: 5 }}>
    <Typography variant="h5" sx={{ mb: 2 }}>
      Top Content
    </Typography>
    <Grid container spacing={2} wrap="nowrap">
      {[...Array(4)].map((_, index) => (
        <Grid item key={index}>
          <ContentBox content="Top Content" />
        </Grid>
      ))}
      <Grid item>
        <SeeMoreBox />
      </Grid>
    </Grid>
  </Box>
);

export default TopContent;
