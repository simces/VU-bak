import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import ProtectedRoute from './components/ProtectedRoute'; 
import UserProfile from './pages/UserProfile';
import PhotoUpload from './pages/PhotoUpload';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route element={<ProtectedRoute />}> {}
          <Route path="/home" element={<Home />} />
          <Route path="/users/:username" element={<UserProfile />} />
          <Route path="/photos/upload" element={<PhotoUpload />} /> 
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;