import React, { useState, useEffect, useCallback } from 'react';
import { Box, Grid, Typography, IconButton } from '@mui/material';
import ContentBox from './ContentBox';
import SeeMoreBox from './SeeMoreBox';
import PhotoBox from './PhotoBox';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import fetchWithToken from '../../utils/fetchUtils';

const ExploreContent = () => {
  const [categories, setCategories] = useState([]);
  const [photos, setPhotos] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [showPhotos, setShowPhotos] = useState(false);
  const [currentCategory, setCurrentCategory] = useState(null);
  const [categoryHistory, setCategoryHistory] = useState([]); 

  const loadCategories = useCallback(async (pageToLoad, category = null) => {
    setLoading(true);
    try {
      const url = category 
        ? `/api/categories/${category}/subcategories?page=${pageToLoad}&size=4` 
        : `/api/categories/top-level?page=${pageToLoad}&size=4`;
      const response = await fetchWithToken(url);
      if (response.length > 0 && response[0].imageUrl) {
        setShowPhotos(true);
        setPhotos(prevPhotos => (pageToLoad === 0 ? response : [...prevPhotos, ...response]));
      } else {
        setShowPhotos(false);
        setCategories(prevCategories => (pageToLoad === 0 ? response : [...prevCategories, ...response.filter(category => !prevCategories.some(c => c.name === category.name))]));
      }
    } catch (error) {
      console.error('Error fetching categories:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  const loadAllPhotos = useCallback(async (category) => {
    setLoading(true);
    try {
      const url = `/api/categories/${category}/content?page=0&size=4`;
      const response = await fetchWithToken(url);
      setShowPhotos(true);
      setPhotos(response);
    } catch (error) {
      console.error('Error fetching photos:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadCategories(page, currentCategory);
  }, [page, currentCategory, loadCategories]);

  const handleLoadMore = () => {
    setPage(prevPage => prevPage + 1);
  };

  const handleCategoryClick = (categoryName) => {
    setCategoryHistory(prevHistory => [...prevHistory, currentCategory]); 
    if (categoryName === "View All") {
      loadAllPhotos(currentCategory);
    } else {
      setCurrentCategory(categoryName);
      setPage(0);
    }
  };

  const handleBack = () => {
    const previousCategory = categoryHistory.pop();
    setCurrentCategory(previousCategory);
    setCategoryHistory([...categoryHistory]); 
    setPage(0); 
  };

  return (
    <Box sx={{ mb: 5 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>
        Explore Content
      </Typography>
      {currentCategory && (
        <IconButton onClick={handleBack} sx={{ mb: 2 }}>
          <ArrowBackIcon />
        </IconButton>
      )}
      <Grid container spacing={2} wrap="nowrap">
        {showPhotos ? (
          photos.map((photo, index) => (
            <Grid item key={index}>
              <PhotoBox imageUrl={photo.imageUrl} photoId={photo.id} />
            </Grid>
          ))
        ) : (
          categories.map((category, index) => (
            <Grid item key={index} onClick={() => handleCategoryClick(category.name)}>
              <ContentBox content={`${category.name} (${category.photoCount})`} />
            </Grid>
          ))
        )}
        {!showPhotos && (
          <Grid item>
            <SeeMoreBox onClick={handleLoadMore} loading={loading} />
          </Grid>
        )}
      </Grid>
    </Box>
  );
};

export default ExploreContent;