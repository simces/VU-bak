import React from 'react';
import '../styles/HomeScreen.css';

import photo1 from '../assets/foto.jpg';
import photo2 from '../assets/23.jpg';
import photo3 from '../assets/34.jpg';

const Header = () => (
  <div className="header">
    <a href="/register">Sign Up</a>
    <span className="divider"></span>
    <a href="/login">Log In</a>
  </div>
);

const MainContent = () => (
  <div className="main-content">
    <h1>Žyminatorius</h1>
    <p>Discover an innovative way to manage and explore photo collections with advanced AI features. Experience seamless search, tagging, and location-based viewing like never before.</p>
  </div>
);

const ImageComponent = ({ image, text }) => (
  <div className="image-component">
    <img src={image} alt={text} />
    <p>{text}</p>
  </div>
);

const ImageGrid = () => (
  <div className="image-grid">
    <ImageComponent image={photo1} text="AI Photo Tagging" />
    <ImageComponent image={photo2} text="Dynamic Search" />
    <ImageComponent image={photo3} text="Location Viewing" />
  </div>
);

function HomeScreen() {
  return (
    <div className="home-screen" style={{ backgroundImage: `url(${photo1})` }}>
      <Header />
      <MainContent />
      <ImageGrid />
    </div>
  );
}

export default HomeScreen;
