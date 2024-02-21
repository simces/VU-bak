import { useNavigate } from 'react-router-dom';
import React, { useState } from 'react';
import '../styles/Register.css';

const Registration = () => {
    const navigate = useNavigate();
    const [userCreationDTO, setUserCreationDTO] = useState({
      username: '',
      email: '',
      password: '',
    });
    const [message, setMessage] = useState('');
  
    const handleChange = (event) => {
      const { name, value } = event.target;
      setUserCreationDTO(prevState => ({
        ...prevState,
        [name]: value
      }));
    };
  
    const handleSubmit = async (e) => {
        e.preventDefault();
        const url = 'http://localhost:8080/api/auth/register';
      try {
        const response = await fetch(url, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(userCreationDTO),
        });
  
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.indexOf("application/json") !== -1) {
          const result = await response.json();
          setMessage(result.message || 'Registration successful!');
          navigate('/login'); // Navigate to /login on success
        } else {
          // Handle non-JSON response
          const textResult = await response.text();
          setMessage(textResult || 'Registration successful!');
          navigate('/login'); // Navigate to /login on success
        }
  
      } catch (error) {
        setMessage('Registration failed: ' + error.message);
      }
    };


    return (
        <div className="form-container"> 
          <h2>Register</h2>
          <form onSubmit={handleSubmit}>
            <div>
              <input
                type="text"
                name="username"
                value={userCreationDTO.username}
                onChange={handleChange}
                placeholder="Username"
                required
              />
            </div>
            <div>
              <input
                type="email"
                name="email"
                value={userCreationDTO.email}
                onChange={handleChange}
                placeholder="Email"
                required
              />
            </div>
            <div>
              <input
                type="password"
                name="password"
                value={userCreationDTO.password}
                onChange={handleChange}
                placeholder="Password"
                required
              />
            </div>
            <button type="submit">Register</button>
          </form>
          {message && <p>{message}</p>}
        </div>
      );
    };

export default Registration;
