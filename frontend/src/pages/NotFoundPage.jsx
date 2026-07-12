import React from 'react';
import { Link } from 'react-router-dom';

export default function NotFoundPage() {
  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      minHeight: '75vh',
      padding: '20px',
      textAlign: 'center',
      color: 'var(--text-cream)'
    }}>
      <div className="desert-card" style={{ maxWidth: '500px' }}>
        <span style={{ fontSize: '72px', display: 'block', marginBottom: '20px', animation: 'engine-rev 0.3s ease-in-out alternate infinite' }}>🌵</span>
        <h1 className="brand-font glow-yellow" style={{ color: 'var(--accent-yellow)', fontSize: '28px', marginBottom: '15px' }}>
          404 - Wrong Turn!
        </h1>
        <h2 className="brand-font" style={{ fontSize: '18px', color: 'var(--secondary-orange)', marginBottom: '20px' }}>
          "You took a wrong turn at Radiator Springs!"
        </h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '14px', lineHeight: '1.6', marginBottom: '30px' }}>
          The road you are looking for has been bypassed by Interstate 40. There's nothing out here but tumbleweeds and cactus. Let's get you back to the main strip!
        </p>
        <Link
          to="/"
          className="nitro-btn"
          style={{
            display: 'inline-block',
            textDecoration: 'none',
            padding: '12px 30px',
            textAlign: 'center'
          }}
        >
          Back to Showroom
        </Link>
      </div>
    </div>
  );
}
