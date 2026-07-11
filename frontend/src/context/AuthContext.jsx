import React, { createContext, useState, useEffect, useContext } from 'react';
import { getCurrentUser, loginUser, registerUser, logoutUser } from '../api/authApi';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Validate existing cookie session on mount (preserve login state on page reload)
  useEffect(() => {
    const checkSession = async () => {
      try {
        const profile = await getCurrentUser();
        setUser(profile);
      } catch (error) {
        setUser(null);
      } finally {
        setLoading(false);
      }
    };
    checkSession();
  }, []);

  const login = async (email, password) => {
    try {
      const data = await loginUser(email, password);
      // Data contains { email, name, role }
      setUser({
        email: data.email,
        name: data.name,
        role: data.role
      });
      return { success: true };
    } catch (error) {
      const message = error.response?.data?.message || 'Login failed. Please check your credentials.';
      return { success: false, error: message };
    }
  };

  const register = async (email, password, name) => {
    try {
      const data = await registerUser(email, password, name);
      setUser({
        email: data.email,
        name: data.name,
        role: data.role
      });
      return { success: true };
    } catch (error) {
      const message = error.response?.data?.message || 'Registration failed. Email might already be taken.';
      return { success: false, error: message };
    }
  };

  const logout = async () => {
    try {
      await logoutUser();
    } catch (error) {
      console.error('Logout error', error);
    } finally {
      setUser(null);
    }
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
