import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { isAdmin } from '../utils/auth';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <header style={{
      padding: '15px 30px',
      background: 'var(--surface-dark)',
      borderBottom: '3px solid var(--primary-red)',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      boxShadow: '0 4px 12px rgba(0,0,0,0.3)'
    }}>
      <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '10px' }}>
        <span style={{ fontSize: '24px' }}>🚗</span>
        <div>
          <h1 style={{
            fontFamily: 'Russo One',
            fontSize: '22px',
            color: 'var(--primary-red)',
            margin: 0,
            lineHeight: 1
          }} className="glow-red">
            Radiator Springs
          </h1>
          <span style={{
            fontSize: '11px',
            color: 'var(--accent-yellow)',
            fontFamily: 'Russo One',
            letterSpacing: '1px'
          }} className="glow-yellow">Dealership</span>
        </div>
      </Link>

      <nav style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        {user ? (
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <Link to="/" style={{
              color: 'var(--text-cream)',
              textDecoration: 'none',
              fontFamily: 'Russo One',
              fontSize: '14px'
            }}>Showroom</Link>
            
            {isAdmin(user) && (
              <Link to="/admin/add" style={{
                color: 'var(--accent-yellow)',
                textDecoration: 'none',
                fontFamily: 'Russo One',
                fontSize: '14px',
                border: '1px solid var(--accent-yellow)',
                padding: '4px 10px',
                borderRadius: 'var(--border-radius-sm)'
              }}>Admin Panel</Link>
            )}

            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span style={{ fontSize: '14px', color: 'var(--text-muted)' }}>Driver:</span>
              <strong style={{ color: 'var(--secondary-orange)' }}>{user.name}</strong>
              <span style={{
                fontSize: '10px',
                background: user.role === 'ADMIN' ? 'var(--primary-red)' : '#333',
                color: 'white',
                padding: '2px 6px',
                borderRadius: '4px',
                fontFamily: 'Russo One'
              }}>{user.role}</span>
            </div>

            <button
              onClick={handleLogout}
              style={{
                background: 'transparent',
                color: 'var(--primary-red)',
                border: '1px solid var(--primary-red)',
                padding: '6px 12px',
                borderRadius: 'var(--border-radius-sm)',
                cursor: 'pointer',
                fontFamily: 'Russo One',
                fontSize: '12px',
                textTransform: 'uppercase',
                transition: 'all 0.2s'
              }}
              onMouseEnter={(e) => {
                e.target.style.background = 'var(--primary-red)';
                e.target.style.color = 'white';
              }}
              onMouseLeave={(e) => {
                e.target.style.background = 'transparent';
                e.target.style.color = 'var(--primary-red)';
              }}
            >
              Kill Engine
            </button>
          </div>
        ) : (
          <div style={{ display: 'flex', gap: '15px' }}>
            <Link to="/login" style={{
              color: 'var(--text-cream)',
              textDecoration: 'none',
              fontFamily: 'Russo One',
              fontSize: '14px',
              padding: '6px 12px'
            }}>Sign In</Link>
            <Link to="/register" style={{
              background: 'var(--primary-red)',
              color: 'white',
              textDecoration: 'none',
              fontFamily: 'Russo One',
              fontSize: '14px',
              padding: '6px 15px',
              borderRadius: 'var(--border-radius-sm)'
            }}>Join Clan</Link>
          </div>
        )}
      </nav>
    </header>
  );
}
