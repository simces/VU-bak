import React from 'react';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token'); // Clear the JWT token
    navigate('/login');
  };

  return (
    <div>
      <h1>Welcome Home!</h1>
      <button onClick = {handleLogout}> Logout </button>
    </div>
  );
};

export default Home;
