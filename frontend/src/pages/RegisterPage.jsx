import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!name.trim() || !email.trim() || !password.trim() || !confirmPassword.trim()) {
      setError('All fuel indicators (fields) are required.');
      return;
    }

    if (password.length < 6) {
      setError('Ignition Key (Password) must be at least 6 characters long.');
      return;
    }

    if (password !== confirmPassword) {
      setError('Ignition Keys (Passwords) do not match.');
      return;
    }

    setSubmitting(true);
    const result = await register(email, password, name);
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
      minHeight: '80vh'
    }}>
      <div className="desert-card">
        <div style={{ textAlign: 'center', marginBottom: '25px' }}>
          <span style={{ fontSize: '40px' }}>🏎️</span>
          <h2 className="brand-font glow-red" style={{ color: 'var(--primary-red)', marginTop: '10px' }}>
            Register Crew
          </h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '14px', marginTop: '5px' }}>
            Create an account to join the racing dealership
          </p>
        </div>

        {error && <div className="leak-alert">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '15px' }}>
            <label style={{
              fontSize: '11px',
              fontFamily: 'Russo One',
              color: 'var(--text-cream)',
              textTransform: 'uppercase'
            }}>
              Driver Name (Full Name)
            </label>
            <input
              type="text"
              className="fuel-input"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. Lightning McQueen"
              required
            />
          </div>

          <div style={{ marginBottom: '15px' }}>
            <label style={{
              fontSize: '11px',
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
              placeholder="e.g. speed@route66.com"
              required
            />
          </div>

          <div style={{ marginBottom: '15px' }}>
            <label style={{
              fontSize: '11px',
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
              placeholder="At least 6 characters"
              required
            />
          </div>

          <div style={{ marginBottom: '25px' }}>
            <label style={{
              fontSize: '11px',
              fontFamily: 'Russo One',
              color: 'var(--text-cream)',
              textTransform: 'uppercase'
            }}>
              Confirm Key (Re-enter Password)
            </label>
            <input
              type="password"
              className="fuel-input"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              placeholder="Re-enter your key"
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
                <span>Entering Pit...</span>
              </div>
            ) : (
              'Join Team'
            )}
          </button>
        </form>

        <div style={{ marginTop: '25px', textAlign: 'center', fontSize: '14px', color: 'var(--text-muted)' }}>
          Already have a crew?{' '}
          <Link to="/login" style={{ color: 'var(--primary-red)', textDecoration: 'none', fontWeight: 'bold' }}>
            Sign In
          </Link>
        </div>
      </div>
    </div>
  );
}
