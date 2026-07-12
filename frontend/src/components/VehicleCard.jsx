import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { isAdmin } from '../utils/auth';

const CATEGORY_GRADIENTS = {
  COUPE: 'linear-gradient(135deg, #c41e3a 0%, #7a0010 100%)',
  SEDAN: 'linear-gradient(135deg, #1f4068 0%, #111e38 100%)',
  TRUCK: 'linear-gradient(135deg, #e87c1e 0%, #874305 100%)',
  VAN: 'linear-gradient(135deg, #2b5c3f 0%, #15301f 100%)',
  HATCHBACK: 'linear-gradient(135deg, #512b58 0%, #2c1630 100%)',
  CONVERTIBLE: 'linear-gradient(135deg, #0f4c81 0%, #082947 100%)',
  SUV: 'linear-gradient(135deg, #3282b8 0%, #1b4965 100%)',
  ELECTRIC: 'linear-gradient(135deg, #00b4d8 0%, #0077b6 100%)',
};

export default function VehicleCard({ vehicle, onPurchase, onEdit, onDelete }) {
  const { user } = useAuth();
  const [purchasing, setPurchasing] = useState(false);

  const gradient = CATEGORY_GRADIENTS[vehicle.category] || 'linear-gradient(135deg, #333 0%, #111 100%)';
  const headerBackground = vehicle.imageUrl
    ? `linear-gradient(rgba(0,0,0,0.2) 0%, rgba(0,0,0,0.85) 100%), url(${vehicle.imageUrl}) center/cover no-repeat`
    : gradient;

  const formattedPrice = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(vehicle.price);

  const handlePurchase = async () => {
    setPurchasing(true);
    await onPurchase(vehicle.id);
    setPurchasing(false);
  };

  // Stock status styling helpers
  let stockLabel = '';
  let stockColor = '';
  let stockBg = '';

  if (vehicle.quantity === 0) {
    stockLabel = '✖ SOLD OUT';
    stockColor = '#ffa0a0';
    stockBg = 'rgba(196, 30, 58, 0.15)';
  } else if (vehicle.quantity <= 2) {
    stockLabel = `⚠ LOW STOCK: ${vehicle.quantity} LEFT`;
    stockColor = '#ffe082';
    stockBg = 'rgba(232, 124, 30, 0.15)';
  } else {
    stockLabel = `✔ IN STOCK: ${vehicle.quantity}`;
    stockColor = '#a8e6cf';
    stockBg = 'rgba(43, 92, 63, 0.15)';
  }

  return (
    <div style={{
      background: 'var(--surface-dark)',
      borderRadius: 'var(--border-radius-lg)',
      border: '1.5px solid var(--surface-dark)',
      overflow: 'hidden',
      display: 'flex',
      flexDirection: 'column',
      boxShadow: '0 6px 18px rgba(0,0,0,0.35)',
      transition: 'all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1)',
    }} className="hover-shake">
      {/* Category header card graphics */}
      <div style={{
        height: '140px',
        background: headerBackground,
        padding: '20px',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        position: 'relative'
      }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
          {/* License plate category badge */}
          <div style={{
            background: '#F5F0E1',
            border: '2px solid #1a1a2e',
            borderRadius: '4px',
            padding: '2px 8px',
            boxShadow: '0 2px 5px rgba(0,0,0,0.3)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}>
            <span style={{
              fontFamily: 'Russo One',
              fontSize: '11px',
              color: '#1b122e',
              letterSpacing: '1px',
            }}>{vehicle.category}</span>
          </div>
          <span style={{
            fontSize: '12px',
            fontFamily: 'Russo One',
            color: 'white',
            background: 'rgba(0,0,0,0.4)',
            padding: '3px 8px',
            borderRadius: '10px'
          }}>{vehicle.year}</span>
        </div>

        <h2 style={{
          color: 'white',
          fontSize: '22px',
          margin: 0,
          textShadow: '2px 2px 4px rgba(0,0,0,0.6)'
        }} className="brand-font">
          {vehicle.make}
        </h2>
      </div>

      {/* Card Body */}
      <div style={{ padding: '20px', flex: 1, display: 'flex', flexDirection: 'column' }}>
        <h3 style={{
          fontSize: '16px',
          color: 'var(--text-cream)',
          marginBottom: '10px',
          textTransform: 'uppercase'
        }} className="brand-font">
          {vehicle.model}
        </h3>

        <p style={{
          fontSize: '14px',
          color: 'var(--text-muted)',
          lineHeight: '1.4',
          marginBottom: '20px',
          flex: 1
        }}>
          {vehicle.description || 'No description loaded.'}
        </p>

        {/* Pricing and Stock Status */}
        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: '20px',
          paddingBottom: '15px',
          borderBottom: '1px solid rgba(255,255,255,0.05)'
        }}>
          <div>
            <span style={{ fontSize: '11px', color: 'var(--text-muted)', display: 'block', textTransform: 'uppercase', fontFamily: 'Russo One' }}>Showroom Price</span>
            <strong style={{ fontSize: '20px', color: 'var(--accent-yellow)' }} className="brand-font glow-yellow">
              {formattedPrice}
            </strong>
          </div>

          <div style={{
            padding: '5px 10px',
            borderRadius: 'var(--border-radius-sm)',
            background: stockBg,
            color: stockColor,
            fontSize: '11px',
            fontFamily: 'Russo One',
            border: `1px solid ${stockColor}33`
          }}>
            {stockLabel}
          </div>
        </div>

        {/* Action Button */}
        <button
          onClick={handlePurchase}
          disabled={vehicle.quantity === 0 || purchasing}
          className="nitro-btn"
          style={{ width: '100%' }}
        >
          {purchasing ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <div className="spinning-tire" style={{ width: '16px', height: '16px', borderWidth: '2.5px' }} />
              <span>Transacting...</span>
            </div>
          ) : vehicle.quantity === 0 ? (
            'Sold Out'
          ) : (
            'Purchase Model'
          )}
        </button>

        {/* Admin Controls */}
        {isAdmin(user) && (
          <div style={{
            display: 'flex',
            gap: '10px',
            marginTop: '12px',
            paddingTop: '12px',
            borderTop: '1px dashed rgba(255,255,255,0.1)'
          }}>
            <button
              onClick={() => onEdit(vehicle.id)}
              style={{
                flex: 1,
                background: 'transparent',
                color: 'var(--secondary-orange)',
                border: '1.5px solid var(--secondary-orange)',
                borderRadius: 'var(--border-radius-sm)',
                padding: '8px',
                fontFamily: 'Russo One',
                fontSize: '11px',
                textTransform: 'uppercase',
                cursor: 'pointer',
                transition: 'all 0.2s'
              }}
              onMouseEnter={(e) => {
                e.target.style.background = 'var(--secondary-orange)';
                e.target.style.color = 'white';
              }}
              onMouseLeave={(e) => {
                e.target.style.background = 'transparent';
                e.target.style.color = 'var(--secondary-orange)';
              }}
            >
              Tune specs
            </button>
            <button
              onClick={() => onDelete(vehicle.id)}
              style={{
                flex: 1,
                background: 'transparent',
                color: 'var(--primary-red)',
                border: '1.5px solid var(--primary-red)',
                borderRadius: 'var(--border-radius-sm)',
                padding: '8px',
                fontFamily: 'Russo One',
                fontSize: '11px',
                textTransform: 'uppercase',
                cursor: 'pointer',
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
              Dismantle
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
