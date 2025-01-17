import React from 'react';
import { Link } from 'react-router-dom';
import { 
  Box, 
  Button, 
  Container, 
  Typography, 
  ThemeProvider, 
  createTheme,
  CssBaseline,
  Grid,
  Card,
  CardContent,
  useMediaQuery
} from '@mui/material';
import { Chat, Security, Speed, Psychology } from '@mui/icons-material';

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

const HomePage = () => {
  const isMobile = useMediaQuery(darkTheme.breakpoints.down('sm'));

  const features = [
    {
      icon: <Chat sx={{ fontSize: 40, color: '#6366f1' }} />,
      title: 'Natural Conversations',
      description: 'Engage in fluid, context-aware conversations with our advanced AI'
    },
    {
      icon: <Security sx={{ fontSize: 40, color: '#6366f1' }} />,
      title: 'Secure Platform',
      description: 'Your conversations are protected with enterprise-grade security'
    },
    {
      icon: <Speed sx={{ fontSize: 40, color: '#6366f1' }} />,
      title: 'Lightning Fast',
      description: 'Get instant responses powered by cutting-edge technology'
    },
    {
      icon: <Psychology sx={{ fontSize: 40, color: '#6366f1' }} />,
      title: 'Smart Learning',
      description: 'AI that adapts to your needs and preferences over time'
    }
  ];

  return (
    <ThemeProvider theme={darkTheme}>
      <CssBaseline />
      <Box
        sx={{
          minHeight: '100vh',
          background: 'linear-gradient(45deg, #0f172a 0%, #1e293b 100%)',
          position: 'relative',
          overflow: 'hidden',
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

        {/* Header Section */}
        <Container maxWidth="lg" sx={{ position: 'relative' }}>
          <Box
            sx={{
              pt: isMobile ? 8 : 15,
              pb: isMobile ? 8 : 12,
              textAlign: 'center',
            }}
          >
            <Typography
              variant="h1"
              sx={{
                fontSize: isMobile ? '2.5rem' : '4rem',
                fontWeight: 700,
                mb: 2,
                background: 'linear-gradient(45deg, #6366f1, #22d3ee)',
                backgroundClip: 'text',
                textFillColor: 'transparent',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
              }}
            >
              SimQuery
            </Typography>
            <Typography
              variant="h2"
              sx={{
                fontSize: isMobile ? '1.5rem' : '2rem',
                mb: 4,
                color: 'rgba(255, 255, 255, 0.8)',
              }}
            >
              Your Intelligent Chat Assistant
            </Typography>
            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', mb: 8 }}>
              <Link to="/register" style={{ textDecoration: 'none' }}>
                <Button
                  variant="contained"
                  size="large"
                  sx={{
                    py: 1.5,
                    px: 4,
                    borderRadius: 2,
                    background: 'linear-gradient(45deg, #6366f1 30%, #22d3ee 90%)',
                    boxShadow: '0 3px 5px 2px rgba(99, 102, 241, .3)',
                    transition: 'all 0.3s ease-in-out',
                    '&:hover': {
                      transform: 'translateY(-2px)',
                      boxShadow: '0 6px 10px 4px rgba(99, 102, 241, .3)',
                    },
                  }}
                >
                  Get Started
                </Button>
              </Link>
              <Link to="/login" style={{ textDecoration: 'none' }}>
                <Button
                  variant="outlined"
                  size="large"
                  sx={{
                    py: 1.5,
                    px: 4,
                    borderRadius: 2,
                    borderColor: '#6366f1',
                    color: '#fff',
                    '&:hover': {
                      borderColor: '#22d3ee',
                      backgroundColor: 'rgba(99, 102, 241, 0.1)',
                    },
                  }}
                >
                  Login
                </Button>
              </Link>
            </Box>
          </Box>

          {/* Features Section */}
          <Grid container spacing={4} sx={{ mb: 8 }}>
            {features.map((feature, index) => (
              <Grid item xs={12} sm={6} md={3} key={index}>
                <Card
                  sx={{
                    height: '100%',
                    backgroundColor: 'background.paper',
                    backdropFilter: 'blur(20px)',
                    border: '1px solid rgba(255, 255, 255, 0.1)',
                    transition: 'transform 0.3s ease-in-out',
                    '&:hover': {
                      transform: 'translateY(-8px)',
                    },
                  }}
                >
                  <CardContent sx={{ textAlign: 'center', p: 3 }}>
                    <Box sx={{ mb: 2 }}>{feature.icon}</Box>
                    <Typography variant="h6" sx={{ mb: 1 }}>
                      {feature.title}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {feature.description}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>
    </ThemeProvider>
  );
};

export default HomePage;