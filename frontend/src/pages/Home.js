import React from 'react';
import SearchComponent from '../components/SearchComponent';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token'); // clear jwt
    navigate('/login');
  };

  return (
    <div>
      <h1>Welcome</h1>
      <SearchComponent />
      <button onClick = {handleLogout}> Logout </button>
    </div>
  );
};

export default Home;
