import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import ProtectedRoute from './routes/ProtectedRoute';
import AdminRoute from './routes/AdminRoute';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import { AddVehiclePage, EditVehiclePage } from './pages/Placeholders';
import './index.css';

function App() {
  return (
    <Router>
      <AuthProvider>
        <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
          {/* Global Header Navigation */}
          <Navbar />
          
          {/* Routing Configuration */}
          <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
            <Routes>
              {/* Public Auth Routes */}
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              
              {/* Protected Showroom Routes */}
              <Route element={<ProtectedRoute />}>
                <Route path="/" element={<DashboardPage />} />
              </Route>

              {/* Admin-Only Management Routes */}
              <Route element={<AdminRoute />}>
                <Route path="/admin/add" element={<AddVehiclePage />} />
                <Route path="/admin/edit/:id" element={<EditVehiclePage />} />
              </Route>
            </Routes>
          </div>

          {/* Global Footer */}
          <Footer />
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
