import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  Button,
  Alert,
  Stack
} from '@mui/material';

const VerifyEmail = () => {
  const navigate = useNavigate();
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [verifying, setVerifying] = useState(false);
  const searchParams = new URLSearchParams(window.location.search);
  const token = searchParams.get('token');

  const verifyEmail = async () => {
    setVerifying(true);
    try {
      const response = await fetch(`http://localhost:8001/api/v1/auth/verify-email?token=${token}`, {
        method: 'GET',
      });
      
      if (!response.ok) {
        throw new Error('Email verification failed');
      }
      
      setMessage('Email verified successfully!');
      setError('');
      setTimeout(() => navigate('/login'), 2000); // Redirect to login after 2 seconds
    } catch (err) {
      setError('Failed to verify email');
      setMessage('');
    } finally {
      setVerifying(false);
    }
  };

  return (
    <Container maxWidth="sm" sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center' }}>
      <Paper elevation={3} sx={{ width: '100%', p: 4, mt: -8 }}>
        <Stack spacing={3}>
          <Typography variant="h4" align="center">
            Email Verification
          </Typography>
          
          {!token && (
            <Typography variant="body1" align="center" color="text.secondary">
              No verification token found. Please check your email for the verification link.
            </Typography>
          )}

          {message && (
            <Alert severity="success">
              {message}
            </Alert>
          )}

          {error && (
            <Alert severity="error">
              {error}
            </Alert>
          )}

          {token && (
            <>
              <Typography variant="body1" align="center" color="text.secondary">
                Click the button below to verify your email
              </Typography>
              <Button
                fullWidth
                variant="contained"
                onClick={verifyEmail}
                disabled={verifying}
              >
                {verifying ? 'Verifying...' : 'Verify Email'}
              </Button>
            </>
          )}

          <Link to="/login" style={{ textDecoration: 'none', textAlign: 'center' }}>
            <Typography color="primary">
              Back to Login
            </Typography>
          </Link>
        </Stack>
      </Paper>
    </Container>
  );
};

export default VerifyEmail;