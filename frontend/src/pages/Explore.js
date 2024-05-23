import React from 'react';
import { Box } from '@mui/material';
import ExploreContent from '../components/explore/ExploreContent';
import TopContent from '../components/explore/TopContent';
import NewContent from '../components/explore/NewContent';
import HotContent from '../components/explore/HotContent';

function Explore() {
  return (
    <Box sx={{ p: 3 }}>
      <ExploreContent />
      <HotContent />
      <NewContent />
      <TopContent />
    </Box>
  );
}

export default Explore;
