import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';

const ContentBox = ({ content }) => (
  <Card sx={{ minWidth: 150, minHeight: 200, m: 1, borderRadius: 2 }}>
    <CardContent>
      <Typography variant="body2" color="text.secondary">
        {content}
      </Typography>
    </CardContent>
  </Card>
);

export default ContentBox;
