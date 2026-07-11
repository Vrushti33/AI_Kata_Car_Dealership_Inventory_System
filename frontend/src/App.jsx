import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import './index.css';

function HomeContent() {
  const { user, loading, logout } = useAuth();

  if (loading) {
    return (
      <div style={{ padding: '40px', textAlign: 'center' }}>
        <h3 className="brand-font glow-yellow">Warming up engines...</h3>
      </div>
    );
  }

  return (
    <main style={{ padding: '40px', maxWidth: '800px', margin: '0 auto' }}>
      <div style={{ background: 'var(--surface-dark)', padding: '30px', borderRadius: 'var(--border-radius-lg)', border: '1px solid var(--secondary-orange)' }} className="hover-shake">
        <h2 style={{ color: 'var(--accent-yellow)', marginBottom: '15px' }} className="brand-font">
          Route 66 Inventory Desk
        </h2>
        {user ? (
          <div>
            <p style={{ fontSize: '18px', marginBottom: '10px' }}>
              Welcome back, <strong style={{ color: 'var(--primary-red)' }}>{user.name}</strong>!
            </p>
            <p style={{ color: 'var(--text-muted)', marginBottom: '20px' }}>
              Role: {user.role} | Email: {user.email}
            </p>
            <button 
              onClick={logout}
              style={{
                background: 'var(--primary-red)',
                color: 'white',
                border: 'none',
                padding: '10px 20px',
                borderRadius: 'var(--border-radius-sm)',
                cursor: 'pointer',
                fontFamily: 'Russo One',
                textTransform: 'uppercase'
              }}
            >
              Kill Engine (Logout)
            </button>
          </div>
        ) : (
          <div>
            <p style={{ marginBottom: '20px' }}>Please log in or register to inspect the showroom floor.</p>
            <div style={{ display: 'flex', gap: '15px' }}>
              <Link 
                to="/login"
                style={{
                  background: 'var(--secondary-orange)',
                  color: 'white',
                  textDecoration: 'none',
                  padding: '10px 20px',
                  borderRadius: 'var(--border-radius-sm)',
                  fontFamily: 'Russo One',
                  textTransform: 'uppercase'
                }}
              >
                Sign In
              </Link>
              <Link 
                to="/register"
                style={{
                  background: 'var(--surface-dark)',
                  color: 'var(--accent-yellow)',
                  border: '1px solid var(--accent-yellow)',
                  textDecoration: 'none',
                  padding: '10px 20px',
                  borderRadius: 'var(--border-radius-sm)',
                  fontFamily: 'Russo One',
                  textTransform: 'uppercase'
                }}
              >
                Join Clan (Register)
              </Link>
            </div>
          </div>
        )}
      </div>
    </main>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="app-container" style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
          <header style={{ padding: '20px', background: 'var(--surface-dark)', borderBottom: '3px solid var(--primary-red)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <div>
              <h1 style={{ fontFamily: 'Russo One', color: 'var(--primary-red)', margin: 0 }} className="glow-red">
                Radiator Springs Dealership
              </h1>
              <p style={{ color: 'var(--accent-yellow)', margin: '5px 0 0 0', fontSize: '14px' }}>Ka-Chow! Speed. I am speed.</p>
            </div>
            <Link to="/" style={{ color: 'var(--text-cream)', textDecoration: 'none', fontFamily: 'Russo One' }}>Showroom</Link>
          </header>

          <Routes>
            <Route path="/" element={<HomeContent />} />
            <Route path="/login" element={<div style={{ padding: '40px', textAlign: 'center' }}><h2 className="brand-font">Login Page Coming in Step 5.2</h2><Link to="/" style={{ color: 'var(--accent-yellow)' }}>Back</Link></div>} />
            <Route path="/register" element={<div style={{ padding: '40px', textAlign: 'center' }}><h2 className="brand-font">Register Page Coming in Step 5.2</h2><Link to="/" style={{ color: 'var(--accent-yellow)' }}>Back</Link></div>} />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
