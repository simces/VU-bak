// src/HomeScreen.js
import React, { useState, useEffect } from 'react';
import '../styles/HomeScreen.css';

import photo1 from '../assets/1.png';
import photo2 from '../assets/2.png';
import photo3 from '../assets/3.png';
import photo4 from '../assets/4.png';

const Header = () => (
    <div className="header">
      <a href="/signup">Sign Up</a>
      <span className="divider"></span>
      <a href="/login">Log In</a>
    </div>
  );
  

const MainContent = () => (
    <div className="main-content">
      <h1>Lorem ipsum dolor sit amet, consectetur (...)</h1>
      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
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
    {[photo1, photo2, photo3, photo4].map((image, index) => (
      <ImageComponent key={index} image={image} text={`Component ${index + 1}`} />
    ))}
  </div>
);

function HomeScreen() {
  const backgrounds = [photo1, photo2, photo3, photo4];
  const [currentBackground, setCurrentBackground] = useState(0);
  const [isScrolled, setIsScrolled] = useState(false);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentBackground((currentBackground) => (currentBackground + 1) % backgrounds.length);
    }, 10000);

    const handleScroll = () => {
      if (!isScrolled && window.scrollY > 0) {
        window.scrollTo({
          top: window.innerHeight,
          behavior: "smooth"
        });
        setIsScrolled(true);
      }
    };

    window.addEventListener('scroll', handleScroll);

    return () => {
      clearInterval(intervalId);
      window.removeEventListener('scroll', handleScroll);
    };
  }, [isScrolled, backgrounds.length]);

  return (
    <div className="home-screen" style={{ backgroundImage: `url(${backgrounds[currentBackground]})` }}>
      <Header />
      <MainContent />
      <ImageGrid />
    </div>
  );
}

export default HomeScreen;
