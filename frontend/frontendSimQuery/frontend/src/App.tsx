import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { PrivateRoute } from './PrivateRoute';
import { PublicOnlyRoute } from './PublicOnlyRoute';
import  Login  from './Login';
import  Register  from './Register';
import  VerifyEmail  from './VerifyEmail';
import { Dashboard } from './Dashboard';
import  HomePage  from './HomePage';

const App = () => {
  return (
    <Router>
      <Routes>
        {/* Public only routes - redirect to dashboard if logged in */}
        <Route
          path="/login"
          element={
            <PublicOnlyRoute>
              <Login />
            </PublicOnlyRoute>
          }
        />
        <Route
          path="/register"
          element={
            <PublicOnlyRoute>
              <Register />
            </PublicOnlyRoute>
          }
        />
        <Route path="/verify-email" element={<VerifyEmail />} />

        {/* Protected routes - require authentication */}
        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />

        {/* Root route */}
        <Route
          path="/"
          element={
            <PublicOnlyRoute>
              <HomePage />
            </PublicOnlyRoute>
          }
        />
      </Routes>
    </Router>
  );
};

export default App;