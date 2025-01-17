export const useAuth = () => {
    const refreshToken = localStorage.getItem('refreshToken');
    return !!refreshToken;
  };