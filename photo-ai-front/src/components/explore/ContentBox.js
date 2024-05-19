import React from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';

const ContentBox = ({ content, imageUrl, likeCount }) => (
  <Card sx={{ width: 250, height: 150, m: 1, borderRadius: 2, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
    {imageUrl ? (
      <Box sx={{ height: '100%', position: 'relative' }}>
        <img src={imageUrl} alt="content" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
        <Box sx={{ position: 'absolute', bottom: 0, left: 0, width: '100%', bgcolor: 'rgba(0, 0, 0, 0.5)', color: 'white', p: 1 }}>
          <Typography variant="body2">{likeCount} likes</Typography>
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

export default ContentBox;
