import React from 'react';
import { Card, CardContent, Typography, CircularProgress } from '@mui/material';

const SeeMoreBox = ({ onClick, loading }) => (
  <Card
    sx={{  width: 250, height: 150, m: 1, borderRadius: 2, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'space-between', cursor: 'pointer' }}
    onClick={onClick}
  >
    <CardContent>
      {loading ? (
        <CircularProgress />
      ) : (
        <Typography variant="body2" color="text.secondary">
          See More
        </Typography>
      )}
    </CardContent>
  </Card>
);

export default SeeMoreBox;
