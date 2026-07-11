import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { searchVehicles, purchaseVehicle, deleteVehicle } from '../api/vehicleApi';
import useDebounce from '../hooks/useDebounce';
import SearchFilterBar from '../components/SearchFilterBar';
import VehicleCard from '../components/VehicleCard';

export default function DashboardPage() {
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [alertMessage, setAlertMessage] = useState({ text: '', type: '' });

  // Filters state
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');

  // Debounced search text
  const debouncedSearch = useDebounce(search, 400);
  const navigate = useNavigate();

  // Fetch vehicles based on filters
  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        setLoading(true);
        const params = {};
        if (debouncedSearch.trim()) params.make = debouncedSearch;
        if (category) params.category = category;
        if (minPrice) params.minPrice = minPrice;
        if (maxPrice) params.maxPrice = maxPrice;

        const data = await searchVehicles(params);
        setVehicles(data);
        setError('');
      } catch (err) {
        setError('Error fetching showroom models. Please verify connection.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicles();
  }, [debouncedSearch, category, minPrice, maxPrice]);

  const handlePurchase = async (id) => {
    try {
      const response = await purchaseVehicle(id);
      
      // Trigger alert banner
      triggerAlert(`Successfully purchased: ${response.vehicleDetails}!`, 'success');

      // Optimistic UI update: decrement stock locally
      setVehicles(prevVehicles =>
        prevVehicles.map(vehicle =>
          vehicle.id === id ? { ...vehicle, quantity: vehicle.quantity - 1 } : vehicle
        )
      );
    } catch (err) {
      const msg = err.response?.data?.message || 'Purchase transaction failed.';
      triggerAlert(msg, 'error');
    }
  };

  const handleEdit = (id) => {
    navigate(`/admin/edit/${id}`);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to dismantle (delete) this model?')) {
      try {
        await deleteVehicle(id);
        triggerAlert('Vehicle dismantled successfully.', 'success');
        
        // Remove from list
        setVehicles(prevVehicles => prevVehicles.filter(vehicle => vehicle.id !== id));
      } catch (err) {
        const msg = err.response?.data?.message || 'Error dismantling vehicle.';
        triggerAlert(msg, 'error');
      }
    }
  };

  const triggerAlert = (text, type) => {
    setAlertMessage({ text, type });
    // Hide alert after 4 seconds
    setTimeout(() => {
      setAlertMessage({ text: '', type: '' });
    }, 4000);
  };

  return (
    <div style={{ padding: '30px', maxWidth: '1200px', margin: '0 auto', width: '100%' }}>
      {/* Show alerts/notices */}
      {alertMessage.text && (
        <div style={{
          position: 'fixed',
          top: '20px',
          right: '20px',
          zIndex: 1000,
          background: alertMessage.type === 'success' ? 'rgba(43, 92, 63, 0.95)' : 'rgba(196, 30, 58, 0.95)',
          color: 'white',
          padding: '15px 25px',
          borderRadius: 'var(--border-radius-md)',
          boxShadow: '0 4px 15px rgba(0,0,0,0.5)',
          fontFamily: 'Russo One',
          fontSize: '14px',
          border: `1.5px solid ${alertMessage.type === 'success' ? '#a8e6cf' : '#ff8a9a'}`,
          animation: 'engine-rev 0.15s ease-in-out alternate'
        }}>
          {alertMessage.type === 'success' ? '🏆 ' : '⚠️ '}
          {alertMessage.text}
        </div>
      )}

      {/* Filter panel console */}
      <SearchFilterBar
        search={search}
        setSearch={setSearch}
        category={category}
        setCategory={setCategory}
        minPrice={minPrice}
        setMinPrice={setMinPrice}
        maxPrice={maxPrice}
        setMaxPrice={setMaxPrice}
      />

      {error && <div className="leak-alert" style={{ marginBottom: '20px' }}>{error}</div>}

      {/* Grid of Vehicles */}
      {loading ? (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '40vh' }}>
          <div className="spinning-tire" />
          <h3 className="brand-font glow-yellow" style={{ marginTop: '20px' }}>Polishing Showroom Models...</h3>
        </div>
      ) : vehicles.length === 0 ? (
        <div style={{
          textAlign: 'center',
          padding: '50px 20px',
          background: 'var(--surface-dark)',
          borderRadius: 'var(--border-radius-lg)',
          border: '1.5px dashed var(--secondary-orange)'
        }}>
          <span style={{ fontSize: '40px' }}>🌵</span>
          <h3 className="brand-font" style={{ color: 'var(--secondary-orange)', marginTop: '15px' }}>Tumbleweeds! No Models Found</h3>
          <p style={{ color: 'var(--text-muted)', marginTop: '5px' }}>Try loosening your console filters.</p>
        </div>
      ) : (
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
          gap: '30px'
        }}>
          {vehicles.map(vehicle => (
            <VehicleCard
              key={vehicle.id}
              vehicle={vehicle}
              onPurchase={handlePurchase}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          ))}
        </div>
      )}
    </div>
  );
}
