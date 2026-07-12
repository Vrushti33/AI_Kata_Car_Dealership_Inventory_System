import React from 'react';

export default class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error("ErrorBoundary caught an error", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '80vh',
          padding: '20px',
          textAlign: 'center',
          color: 'var(--text-cream)'
        }}>
          <div className="desert-card" style={{ maxWidth: '500px', border: '2px solid var(--primary-red)' }}>
            <span style={{ fontSize: '64px', animation: 'engine-rev 0.2s infinite' }}>💥</span>
            <h2 className="brand-font glow-yellow" style={{ color: 'var(--primary-red)', marginTop: '20px', fontSize: '24px' }}>
              Engine Blown!
            </h2>
            <p style={{ marginTop: '15px', color: 'var(--text-cream)', fontSize: '14px', lineHeight: '1.6' }}>
              We hit a severe system leak and had to turn off the engine. Please try reloading the page or head back to the showroom.
            </p>
            {this.state.error && (
              <pre style={{
                marginTop: '15px',
                padding: '10px',
                background: 'rgba(0,0,0,0.5)',
                color: '#ff8a9a',
                borderRadius: 'var(--border-radius-sm)',
                fontSize: '11px',
                overflowX: 'auto',
                textAlign: 'left'
              }}>
                {this.state.error.toString()}
              </pre>
            )}
            <button
              onClick={() => window.location.reload()}
              className="nitro-btn"
              style={{ marginTop: '25px', width: '100%', padding: '12px' }}
            >
              Restart Engine (Reload)
            </button>
          </div>
        </div>
      );
    }

    return this.props.children;
  }
}
