import api from './axios';

export const getAllVehicles = async () => {
  const response = await api.get('/vehicles');
  return response.data;
};

export const searchVehicles = async (params) => {
  const response = await api.get('/vehicles/search', { params });
  return response.data;
};

export const createVehicle = async (vehicleData) => {
  const response = await api.post('/vehicles', vehicleData);
  return response.data;
};

export const updateVehicle = async (id, vehicleData) => {
  const response = await api.put(`/vehicles/${id}`, vehicleData);
  return response.data;
};

export const deleteVehicle = async (id) => {
  await api.delete(`/vehicles/${id}`);
};

export const purchaseVehicle = async (id) => {
  const response = await api.post(`/vehicles/${id}/purchase`);
  return response.data;
};

export const restockVehicle = async (id, quantity) => {
  const response = await api.post(`/vehicles/${id}/restock`, { quantity });
  return response.data;
};
export const getVehicleById = async (id) => {
  const response = await api.get(`/vehicles/${id}`);
  return response.data;
};

export const getMyPurchases = async () => {
  const response = await api.get('/vehicles/purchases/my');
  return response.data;
};

export const getAllPurchases = async () => {
  const response = await api.get('/vehicles/purchases');
  return response.data;
};

