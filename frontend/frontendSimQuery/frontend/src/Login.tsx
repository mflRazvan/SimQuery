import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  Alert,
  ThemeProvider,
  createTheme,
  CssBaseline,
  Stack
} from '@mui/material';
import { Login as LoginIcon } from '@mui/icons-material';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#6366f1',
    },
    secondary: {
      main: '#22d3ee',
    },
    background: {
      default: '#0f172a',
      paper: 'rgba(30, 41, 59, 0.8)',
    },
  },
});

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8001/api/v1/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });
      
      if (!response.ok) {
        throw new Error('Login failed');
      }
      
      const data = await response.json();
      localStorage.setItem('accessToken', data.accessToken);
      localStorage.setItem('refreshToken', data.refreshToken);

      navigate('/dashboard');
    } catch (err) {
      setError('Invalid credentials');
    }
  };

  return (
    <ThemeProvider theme={darkTheme}>
      <CssBaseline />
      <Box
        sx={{
          minHeight: '100vh',
          background: 'linear-gradient(45deg, #0f172a 0%, #1e293b 100%)',
          position: 'relative',
          overflow: 'hidden',
          display: 'flex',
          alignItems: 'center',
        }}
      >
        {/* Animated background effects */}
        <Box
          sx={{
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundImage: `
              radial-gradient(circle at 20% 30%, rgba(99, 102, 241, 0.15) 0%, transparent 30%),
              radial-gradient(circle at 80% 70%, rgba(34, 211, 238, 0.15) 0%, transparent 30%)
            `,
            animation: 'gradient 15s ease infinite',
            '@keyframes gradient': {
              '0%': { transform: 'scale(1, 1)' },
              '50%': { transform: 'scale(1.2, 1.2)' },
              '100%': { transform: 'scale(1, 1)' },
            },
          }}
        />

        <Container maxWidth="sm" sx={{ position: 'relative', zIndex: 1 }}>
          <Paper
            elevation={0}
            sx={{
              p: 4,
              backgroundColor: 'background.paper',
              backdropFilter: 'blur(20px)',
              border: '1px solid rgba(255, 255, 255, 0.1)',
              borderRadius: 2,
            }}
          >
            <Stack spacing={3}>
              <Box sx={{ textAlign: 'center', mb: 2 }}>
                <LoginIcon sx={{ 
                  fontSize: 48, 
                  mb: 2,
                  background: 'linear-gradient(45deg, #6366f1, #22d3ee)',
                  borderRadius: '50%',
                  p: 1,
                  color: 'white'
                }} />
                <Typography
                  variant="h4"
                  sx={{
                    fontWeight: 700,
                    background: 'linear-gradient(45deg, #6366f1, #22d3ee)',
                    backgroundClip: 'text',
                    textFillColor: 'transparent',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                  }}
                >
                  Welcome Back
                </Typography>
                <Typography variant="body1" sx={{ color: 'rgba(255, 255, 255, 0.7)', mt: 1 }}>
                  Login to continue your conversation
                </Typography>
              </Box>

              {error && (
                <Alert 
                  severity="error"
                  sx={{
                    backgroundColor: 'rgba(211, 47, 47, 0.1)',
                    color: '#ff5252',
                    border: '1px solid rgba(211, 47, 47, 0.2)',
                  }}
                >
                  {error}
                </Alert>
              )}

              <Box component="form" onSubmit={handleSubmit}>
                <TextField
                  margin="normal"
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  value={formData.email}
                  onChange={(e) => setFormData({...formData, email: e.target.value})}
                  sx={{
                    '& .MuiOutlinedInput-root': {
                      backgroundColor: 'rgba(255, 255, 255, 0.05)',
                      '& fieldset': {
                        borderColor: 'rgba(255, 255, 255, 0.1)',
                      },
                      '&:hover fieldset': {
                        borderColor: '#6366f1',
                      },
                      '&.Mui-focused fieldset': {
                        borderColor: '#22d3ee',
                      },
                    },
                    '& label.Mui-focused': {
                      color: '#22d3ee',
                    },
                  }}
                />
                <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  value={formData.password}
                  onChange={(e) => setFormData({...formData, password: e.target.value})}
                  sx={{
                    '& .MuiOutlinedInput-root': {
                      backgroundColor: 'rgba(255, 255, 255, 0.05)',
                      '& fieldset': {
                        borderColor: 'rgba(255, 255, 255, 0.1)',
                      },
                      '&:hover fieldset': {
                        borderColor: '#6366f1',
                      },
                      '&.Mui-focused fieldset': {
                        borderColor: '#22d3ee',
                      },
                    },
                    '& label.Mui-focused': {
                      color: '#22d3ee',
                    },
                  }}
                />
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 3,
                    mb: 2,
                    py: 1.5,
                    background: 'linear-gradient(45deg, #6366f1 30%, #22d3ee 90%)',
                    boxShadow: '0 3px 5px 2px rgba(99, 102, 241, .3)',
                    transition: 'all 0.3s ease-in-out',
                    '&:hover': {
                      transform: 'translateY(-2px)',
                      boxShadow: '0 6px 10px 4px rgba(99, 102, 241, .3)',
                    },
                  }}
                >
                  Login
                </Button>
              </Box>

              <Link 
                to="/register" 
                style={{ 
                  textDecoration: 'none', 
                  textAlign: 'center',
                  display: 'block'
                }}
              >
                <Typography
                  sx={{
                    color: '#6366f1',
                    transition: 'all 0.3s ease',
                    '&:hover': {
                      color: '#22d3ee',
                    },
                  }}
                >
                  Don't have an account? Register
                </Typography>
              </Link>
            </Stack>
          </Paper>
        </Container>
      </Box>
    </ThemeProvider>
  );
};

export default Login;