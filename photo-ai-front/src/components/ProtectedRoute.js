import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const useAuth = () => {
  const token = localStorage.getItem('token');
  return !!token; // Returns true if token exists (user is authenticated)
};

// This component will be used for protected routes like /home and /feed
const ProtectedRoute = () => {
  const isAuthenticated = useAuth();
  return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;