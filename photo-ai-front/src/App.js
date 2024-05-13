import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import ProtectedRoute from './components/ProtectedRoute'; 
import PublicRoute from './components/PublicRoute';
import UserProfile from './pages/UserProfile';
import PhotoUpload from './pages/PhotoUpload';
import Registration from './pages/Registration';
import PhotoDetails from './pages/PhotoDetails';
import ChangeDetails from './pages/ChangeDetails';
import Feed from './pages/Feed';
import HomeScreen from './pages/HomeScreen';
import Admin from './pages/Admin';
import NotFound from './pages/NotFound';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<PublicRoute />}>
          <Route path="/" element={<HomeScreen />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Registration />} />
        </Route>
        <Route element={<ProtectedRoute />}>
          <Route path="/home" element={<Home />} />
          <Route path="/feed" element={<Feed />} />
          <Route path="/users/:username" element={<UserProfile />} />
          <Route path="/photos/upload" element={<PhotoUpload />} />
          <Route path="/photos/:photoId" element={<PhotoDetails />} />
          <Route path="/edit-profile" element={<ChangeDetails />} />
          <Route path="/admin" element={<Admin />} />
        </Route>
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;