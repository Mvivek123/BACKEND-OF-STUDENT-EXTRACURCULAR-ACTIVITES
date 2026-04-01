import React, { useState } from 'react';
import ActivitiesList from './components/ActivitiesList';
import Login from './components/Login';
import AuthService from './components/AuthService';

export default function App() {
  const [user, setUser] = useState(AuthService.getUser());

  const handleLoginSuccess = (res) => {
    setUser(AuthService.getUser());
  };

  const handleLogout = () => {
    AuthService.logout();
    setUser(null);
  };

  return (
    <div className="container">
      <h1 className="header">Campus Activities</h1>
      {user ? (
        <div>
          <div style={{ marginBottom: 12 }}>
            Logged in as {user.name || user.username} <button className="button" onClick={handleLogout}>Logout</button>
          </div>
          <ActivitiesList />
        </div>
      ) : (
        <Login onLoginSuccess={handleLoginSuccess} />
      )}
    </div>
  );
}
