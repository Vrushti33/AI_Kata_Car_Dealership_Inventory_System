import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    if (!email.trim() || !password.trim()) {
      setError('Please fill in all fuel indicators (fields).');
      return;
    }

    setSubmitting(true);
    const result = await login(email, password);
    setSubmitting(false);

    if (result.success) {
      navigate('/');
    } else {
      setError(result.error);
    }
  };

  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '40px 20px',
      minHeight: '75vh'
    }}>
      <div className="desert-card">
        <div style={{ textAlign: 'center', marginBottom: '30px' }}>
          <span style={{ fontSize: '40px' }}>🔑</span>
          <h2 className="brand-font glow-yellow" style={{ color: 'var(--accent-yellow)', marginTop: '10px' }}>
            Check In Desk
          </h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '14px', marginTop: '5px' }}>
            Authenticate to access the showroom floor
          </p>
        </div>

        {error && <div className="leak-alert">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '20px' }}>
            <label style={{
              fontSize: '12px',
              fontFamily: 'Russo One',
              color: 'var(--text-cream)',
              textTransform: 'uppercase'
            }}>
              Fuel Injector (Email)
            </label>
            <input
              type="email"
              className="fuel-input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="e.g. mcqueen@route66.com"
              required
            />
          </div>

          <div style={{ marginBottom: '30px' }}>
            <label style={{
              fontSize: '12px',
              fontFamily: 'Russo One',
              color: 'var(--text-cream)',
              textTransform: 'uppercase'
            }}>
              Ignition Key (Password)
            </label>
            <input
              type="password"
              className="fuel-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your key"
              required
            />
          </div>

          <button 
            type="submit" 
            className="nitro-btn"
            disabled={submitting}
          >
            {submitting ? (
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <div className="spinning-tire" style={{ width: '20px', height: '20px', borderWidth: '3px' }} />
                <span>Firing Engines...</span>
              </div>
            ) : (
              'Ignite Motor'
            )}
          </button>
        </form>

        <div style={{ marginTop: '25px', textAlign: 'center', fontSize: '14px', color: 'var(--text-muted)' }}>
          New to Radiator Springs?{' '}
          <Link to="/register" style={{ color: 'var(--accent-yellow)', textDecoration: 'none', fontWeight: 'bold' }}>
            Join Clan (Register)
          </Link>
        </div>
      </div>
    </div>
  );
}
