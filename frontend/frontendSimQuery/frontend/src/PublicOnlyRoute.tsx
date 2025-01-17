import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './useAuth';

interface PublicOnlyRouteProps {
  children: React.ReactNode;
}

export const PublicOnlyRoute = ({ children }: PublicOnlyRouteProps) => {
  const isAuthenticated = useAuth();
  const navigate = useNavigate();

  React.useEffect(() => {
    if (isAuthenticated) {
      navigate('/dashboard');
    }
  }, [isAuthenticated, navigate]);

  if (isAuthenticated) {
    return null;
  }

  return <>{children}</>;
};