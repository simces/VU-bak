import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.css';
import App from './App';
import reportWebVitals from './utils/reportWebVitals';
import Modal from 'react-modal';

const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement);

Modal.setAppElement(rootElement);

root.render(
  
  // otherwise, for some reason, renders the initial 
  // cards twice in the /explore page

  // <React.StrictMode>
    <App />
  // </React.StrictMode>
);

reportWebVitals();