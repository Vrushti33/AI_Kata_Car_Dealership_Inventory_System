import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getVehicleById, updateVehicle } from '../api/vehicleApi';
import VehicleForm from '../components/VehicleForm';

export default function EditVehiclePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [vehicle, setVehicle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchVehicle = async () => {
      try {
        setLoading(true);
        const data = await getVehicleById(id);
        setVehicle(data);
      } catch (err) {
        setError('Error loading specifications for this vehicle.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchVehicle();
  }, [id]);

  const handleEditSubmit = async (formData) => {
    await updateVehicle(id, formData);
    navigate('/');
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '60vh' }}>
        <div className="spinning-tire" />
        <h3 className="brand-font glow-yellow" style={{ marginTop: '20px' }}>Loading Specs...</h3>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ padding: '40px', textAlign: 'center' }}>
        <div className="leak-alert" style={{ display: 'inline-block' }}>{error}</div>
      </div>
    );
  }

  return (
    <VehicleForm
      initialData={vehicle}
      onSubmit={handleEditSubmit}
      title="Tune Specifications"
      buttonText="Update Specifications"
    />
  );
}
