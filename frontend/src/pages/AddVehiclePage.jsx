import React from 'react';
import { useNavigate } from 'react-router-dom';
import { createVehicle } from '../api/vehicleApi';
import VehicleForm from '../components/VehicleForm';

export default function AddVehiclePage() {
  const navigate = useNavigate();

  const handleAddSubmit = async (formData) => {
    await createVehicle(formData);
    // Redirect to showroom on success
    navigate('/');
  };

  return (
    <VehicleForm
      onSubmit={handleAddSubmit}
      title="Import New Model"
      buttonText="Import Vehicle"
    />
  );
}
