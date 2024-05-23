import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const useAuth = () => {
  const token = localStorage.getItem('token');
  return !!token; // Returns true if token exists (user is authenticated)
};

const PublicRoute = () => {
  const isAuthenticated = useAuth();
  return isAuthenticated ? <Navigate to="/home" replace /> : <Outlet />;
};

export default PublicRoute;