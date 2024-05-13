import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const useAuth = () => {
  const token = localStorage.getItem('token');
  return !!token; // Returns true if token exists (user is authenticated)
};

// This component will be used for public routes like /login, /register, and /
const PublicRoute = () => {
  const isAuthenticated = useAuth();
  return isAuthenticated ? <Navigate to="/home" replace /> : <Outlet />;
};

export default PublicRoute;