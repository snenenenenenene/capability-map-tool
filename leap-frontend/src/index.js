import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import App from './App'
import { Auth0Provider } from '@auth0/auth0-react';


ReactDOM.render(
  <React.StrictMode>
      <Auth0Provider
        domain="dev-94uwxr97.eu.auth0.com"
        clientId="NPIzze5rZdtNhJyHibQIKmZwn5iLWIaY"
        redirectUri={window.location.origin}
        audience="https://dev-94uwxr97.eu.auth0.com/api/v2/"
        scope="read:current_user"
      >
        <App />
      </Auth0Provider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
