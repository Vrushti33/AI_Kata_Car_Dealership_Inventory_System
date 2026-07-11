import React from 'react';

export default function Footer() {
  return (
    <footer style={{
      marginTop: 'auto',
      background: 'var(--surface-dark)',
      borderTop: '2px solid var(--secondary-orange)',
      padding: '20px',
      textAlign: 'center',
      fontSize: '14px',
      color: 'var(--text-muted)'
    }}>
      <div style={{ display: 'flex', justifyContent: 'center', gap: '8px', marginBottom: '8px' }}>
        <span style={{ color: 'var(--accent-yellow)' }}>🏁</span>
        <span className="brand-font" style={{ color: 'var(--text-cream)' }}>Radiator Springs Dealership</span>
        <span style={{ color: 'var(--accent-yellow)' }}>🏁</span>
      </div>
      <p style={{ letterSpacing: '0.5px' }}>
        Welcome to the Desert! All rights reserved. 
      </p>
      
      {/* Decorative road lanes */}
      <div style={{
        marginTop: '15px',
        height: '4px',
        background: 'repeating-linear-gradient(90deg, var(--accent-yellow) 0px, var(--accent-yellow) 20px, transparent 20px, transparent 40px)',
        opacity: 0.6
      }} />
    </footer>
  );
}
