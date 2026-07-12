import React, { useEffect, useState } from 'react';
import { getMyPurchases, getAllPurchases } from '../api/vehicleApi';
import { useAuth } from '../context/AuthContext';
import { isAdmin } from '../utils/auth';

export default function PurchasesPage() {
  const { user } = useAuth();
  const [purchases, setPurchases] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchPurchases = async () => {
      try {
        setLoading(true);
        const data = isAdmin(user) ? await getAllPurchases() : await getMyPurchases();
        // Sort by purchase time descending to show newest first
        if (Array.isArray(data)) {
          data.sort((a, b) => new Date(b.purchasedAt) - new Date(a.purchasedAt));
          setPurchases(data);
        }
      } catch (err) {
        console.error("Failed to load purchases", err);
        setError("Could not retrieve purchase logs. Please check connection.");
      } finally {
        setLoading(false);
      }
    };
    fetchPurchases();
  }, [user]);

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(price);
  };

  return (
    <div className="page-fade-in" style={{ padding: '30px', maxWidth: '1000px', margin: '0 auto', width: '100%' }}>
      <div style={{ textAlign: 'center', marginBottom: '40px' }}>
        <h1 className="brand-font glow-yellow" style={{ color: 'var(--accent-yellow)', fontSize: '32px' }}>
          {isAdmin(user) ? '🏎️ System Transaction Ledger' : '🏁 Your Showroom Receipts'}
        </h1>
        <p style={{ color: 'var(--text-muted)', marginTop: '8px' }}>
          {isAdmin(user) ? 'Monitor all model acquisitions executed across the dealership' : 'Track tuned specs and catalog orders you have purchased'}
        </p>
      </div>

      {error && <div className="leak-alert">{error}</div>}

      {loading ? (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '30vh' }}>
          <div className="spinning-tire" />
          <h3 className="brand-font glow-yellow" style={{ marginTop: '20px' }}>Syncing Log Records...</h3>
        </div>
      ) : purchases.length === 0 ? (
        <div style={{
          textAlign: 'center',
          padding: '50px 20px',
          background: 'var(--surface-dark)',
          borderRadius: 'var(--border-radius-lg)',
          border: '1.5px dashed var(--secondary-orange)'
        }}>
          <span style={{ fontSize: '40px' }}>🌵</span>
          <h3 className="brand-font" style={{ color: 'var(--secondary-orange)', marginTop: '15px' }}>No Transactions Recorded</h3>
          <p style={{ color: 'var(--text-muted)', marginTop: '5px' }}>
            {isAdmin(user) ? 'Tumbleweeds! No purchases exist in the system database yet.' : 'You have not acquired any models yet. Head to the showroom to make a purchase!'}
          </p>
        </div>
      ) : (
        <div style={{
          overflowX: 'auto',
          background: 'var(--surface-dark)',
          border: '2px solid var(--secondary-orange)',
          borderRadius: 'var(--border-radius-lg)',
          boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.4)'
        }}>
          <table style={{
            width: '100%',
            borderCollapse: 'collapse',
            textAlign: 'left',
            color: 'var(--text-cream)'
          }}>
            <thead>
              <tr style={{ borderBottom: '2px solid var(--secondary-orange)', background: 'rgba(232, 124, 30, 0.1)' }}>
                <th style={{ padding: '15px 20px', fontFamily: 'Russo One', fontSize: '12px', textTransform: 'uppercase' }}>Log ID</th>
                {isAdmin(user) && (
                  <>
                    <th style={{ padding: '15px 20px', fontFamily: 'Russo One', fontSize: '12px', textTransform: 'uppercase' }}>Buyer Name</th>
                    <th style={{ padding: '15px 20px', fontFamily: 'Russo One', fontSize: '12px', textTransform: 'uppercase' }}>Buyer Email</th>
                  </>
                )}
                <th style={{ padding: '15px 20px', fontFamily: 'Russo One', fontSize: '12px', textTransform: 'uppercase' }}>Vehicle Model</th>
                <th style={{ padding: '15px 20px', fontFamily: 'Russo One', fontSize: '12px', textTransform: 'uppercase', textAlign: 'center' }}>Qty</th>
                <th style={{ padding: '15px 20px', fontFamily: 'Russo One', fontSize: '12px', textTransform: 'uppercase' }}>Total Paid</th>
                <th style={{ padding: '15px 20px', fontFamily: 'Russo One', fontSize: '12px', textTransform: 'uppercase' }}>Timestamp</th>
              </tr>
            </thead>
            <tbody>
              {purchases.map((p, idx) => (
                <tr key={p.purchaseId} style={{
                  borderBottom: '1px solid rgba(255,255,255,0.05)',
                  background: idx % 2 === 0 ? 'transparent' : 'rgba(255,255,255,0.02)'
                }}>
                  <td style={{ padding: '15px 20px', fontFamily: 'monospace', color: 'var(--text-muted)' }}>#{p.purchaseId}</td>
                  {isAdmin(user) && (
                    <>
                      <td style={{ padding: '15px 20px', fontWeight: 'bold' }}>{p.buyerName || 'N/A'}</td>
                      <td style={{ padding: '15px 20px', color: 'var(--text-muted)' }}>{p.buyerEmail || 'N/A'}</td>
                    </>
                  )}
                  <td style={{ padding: '15px 20px', color: 'var(--accent-yellow)', fontWeight: 'bold' }}>{p.vehicleDetails || 'Unknown Model'}</td>
                  <td style={{ padding: '15px 20px', textAlign: 'center', fontFamily: 'Russo One' }}>{p.quantity}</td>
                  <td style={{ padding: '15px 20px', color: 'white', fontWeight: 'bold' }}>{formatPrice(p.totalPrice)}</td>
                  <td style={{ padding: '15px 20px', color: 'var(--text-muted)', fontSize: '13px' }}>{p.purchasedAt ? formatDate(p.purchasedAt) : 'N/A'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
