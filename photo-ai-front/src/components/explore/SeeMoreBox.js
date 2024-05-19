import React from 'react';
import { Card, CardContent, Typography, CircularProgress } from '@mui/material';

const SeeMoreBox = ({ onClick, loading }) => (
  <Card
    sx={{ minWidth: 150, minHeight: 200, m: 1, borderRadius: 2, display: 'flex', justifyContent: 'center', alignItems: 'center', cursor: 'pointer' }}
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
