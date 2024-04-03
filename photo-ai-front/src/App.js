import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import ProtectedRoute from './components/ProtectedRoute'; 
import UserProfile from './pages/UserProfile';
import PhotoUpload from './pages/PhotoUpload';
import Registration from './pages/Registration';
import PhotoDetails from './pages/PhotoDetails';
import ChangeDetails from './pages/ChangeDetails';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Registration />} />
        <Route element={<ProtectedRoute />}> {}
          <Route path="/home" element={<Home />} />
          <Route path="/users/:username" element={<UserProfile />} />
          <Route path="/photos/upload" element={<PhotoUpload />} /> 
          <Route path="/photos/:photoId" element={<PhotoDetails />} />
          <Route path="edit-profile" element={<ChangeDetails />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;