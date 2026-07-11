import React from 'react';
import { Link } from 'react-router-dom';

export function AddVehiclePage() {
  return (
    <div style={{ padding: '40px', maxWidth: '600px', margin: '0 auto', textAlign: 'center' }}>
      <h2 className="brand-font glow-red" style={{ color: 'var(--primary-red)', marginBottom: '20px' }}>
        Import New Model (Add Vehicle)
      </h2>
      <p style={{ color: 'var(--text-muted)' }}>
        This is a placeholder for the Admin Add Vehicle Page.
      </p>
      <Link to="/" style={{ color: 'var(--accent-yellow)', marginTop: '20px', display: 'inline-block' }}>Back to Showroom</Link>
    </div>
  );
}

export function EditVehiclePage() {
  return (
    <div style={{ padding: '40px', maxWidth: '600px', margin: '0 auto', textAlign: 'center' }}>
      <h2 className="brand-font glow-yellow" style={{ color: 'var(--secondary-orange)', marginBottom: '20px' }}>
        Modify Specifications (Edit Vehicle)
      </h2>
      <p style={{ color: 'var(--text-muted)' }}>
        This is a placeholder for the Admin Edit Vehicle Page.
      </p>
      <Link to="/" style={{ color: 'var(--accent-yellow)', marginTop: '20px', display: 'inline-block' }}>Back to Showroom</Link>
    </div>
  );
}
