import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

const CATEGORIES = ['SEDAN', 'SUV', 'TRUCK', 'COUPE', 'CONVERTIBLE', 'HATCHBACK', 'VAN', 'ELECTRIC'];

export default function VehicleForm({ initialData, onSubmit, title, buttonText }) {
  const [formData, setFormData] = useState({
    make: '',
    model: '',
    year: new Date().getFullYear(),
    category: 'COUPE',
    price: '',
    quantity: '',
    description: '',
    imageUrl: ''
  });

  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (initialData) {
      setFormData({
        make: initialData.make || '',
        model: initialData.model || '',
        year: initialData.year || new Date().getFullYear(),
        category: initialData.category || 'COUPE',
        price: initialData.price || '',
        quantity: initialData.quantity || '',
        description: initialData.description || '',
        imageUrl: initialData.imageUrl || ''
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear field-specific error as they type
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const validate = () => {
    const newErrors = {};
    const currentYear = new Date().getFullYear();

    if (!formData.make.trim()) newErrors.make = 'Make is required.';
    if (!formData.model.trim()) newErrors.model = 'Model is required.';
    
    // Year validation
    const yearNum = parseInt(formData.year, 10);
    if (!formData.year || isNaN(yearNum) || yearNum < 1886 || yearNum > currentYear + 1) {
      newErrors.year = `Year must be between 1886 and ${currentYear + 1}.`;
    }

    if (!formData.category) newErrors.category = 'Category is required.';

    // Price validation
    const priceNum = parseFloat(formData.price);
    if (formData.price === '' || isNaN(priceNum) || priceNum <= 0) {
      newErrors.price = 'Price must be a positive decimal greater than 0.';
    }

    // Quantity validation
    const qtyNum = parseInt(formData.quantity, 10);
    if (formData.quantity === '' || isNaN(qtyNum) || qtyNum < 0) {
      newErrors.quantity = 'Quantity must be a positive integer >= 0.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setSubmitting(true);
    try {
      // Cast fields before submitting
      const finalData = {
        ...formData,
        year: parseInt(formData.year, 10),
        price: parseFloat(formData.price),
        quantity: parseInt(formData.quantity, 10),
        imageUrl: formData.imageUrl.trim() || null,
        description: formData.description.trim() || null
      };
      await onSubmit(finalData);
    } catch (err) {
      console.error(err);
      setErrors(prev => ({
        ...prev,
        submit: err.response?.data?.message || 'Server error saving specs.'
      }));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      padding: '40px 20px',
      minHeight: '80vh',
      width: '100%'
    }}>
      <div className="desert-card" style={{ maxWidth: '600px' }}>
        <h2 className="brand-font glow-yellow" style={{ color: 'var(--accent-yellow)', marginBottom: '10px', textAlign: 'center' }}>
          {title}
        </h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '14px', marginBottom: '25px', textAlign: 'center' }}>
          Configure specifications and showroom metrics
        </p>

        {errors.submit && <div className="leak-alert">{errors.submit}</div>}

        <form onSubmit={handleSubmit}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' }}>
            {/* Make */}
            <div>
              <label style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)', textTransform: 'uppercase' }}>
                Make (Manufacturer)
              </label>
              <input
                type="text"
                name="make"
                className="fuel-input"
                value={formData.make}
                onChange={handleChange}
                placeholder="e.g. Lightning"
                required
              />
              {errors.make && <span style={{ color: '#ff8a9a', fontSize: '11px' }}>{errors.make}</span>}
            </div>

            {/* Model */}
            <div>
              <label style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)', textTransform: 'uppercase' }}>
                Model Name
              </label>
              <input
                type="text"
                name="model"
                className="fuel-input"
                value={formData.model}
                onChange={handleChange}
                placeholder="e.g. McQueen Special"
                required
              />
              {errors.model && <span style={{ color: '#ff8a9a', fontSize: '11px' }}>{errors.model}</span>}
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' }}>
            {/* Year */}
            <div>
              <label style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)', textTransform: 'uppercase' }}>
                Manufacture Year
              </label>
              <input
                type="number"
                name="year"
                className="fuel-input"
                value={formData.year}
                onChange={handleChange}
                placeholder="2024"
                required
              />
              {errors.year && <span style={{ color: '#ff8a9a', fontSize: '11px' }}>{errors.year}</span>}
            </div>

            {/* Category */}
            <div>
              <label style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)', textTransform: 'uppercase' }}>
                Body Category
              </label>
              <select
                name="category"
                className="fuel-input"
                value={formData.category}
                onChange={handleChange}
                required
              >
                {CATEGORIES.map(cat => (
                  <option key={cat} value={cat}>{cat}</option>
                ))}
              </select>
              {errors.category && <span style={{ color: '#ff8a9a', fontSize: '11px' }}>{errors.category}</span>}
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' }}>
            {/* Price */}
            <div>
              <label style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)', textTransform: 'uppercase' }}>
                Showroom Price ($)
              </label>
              <input
                type="number"
                name="price"
                className="fuel-input"
                value={formData.price}
                onChange={handleChange}
                placeholder="95000.00"
                step="0.01"
                min="0.01"
                required
              />
              {errors.price && <span style={{ color: '#ff8a9a', fontSize: '11px' }}>{errors.price}</span>}
            </div>

            {/* Quantity */}
            <div>
              <label style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)', textTransform: 'uppercase' }}>
                Stock Quantity
              </label>
              <input
                type="number"
                name="quantity"
                className="fuel-input"
                value={formData.quantity}
                onChange={handleChange}
                placeholder="5"
                step="1"
                min="0"
                required
              />
              {errors.quantity && <span style={{ color: '#ff8a9a', fontSize: '11px' }}>{errors.quantity}</span>}
            </div>
          </div>

          {/* Image URL */}
          <div style={{ marginBottom: '15px' }}>
            <label style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)', textTransform: 'uppercase' }}>
              Image URL
            </label>
            <input
              type="url"
              name="imageUrl"
              className="fuel-input"
              value={formData.imageUrl}
              onChange={handleChange}
              placeholder="https://example.com/mcqueen.png"
            />
          </div>

          {/* Description */}
          <div style={{ marginBottom: '25px' }}>
            <label style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)', textTransform: 'uppercase' }}>
              Showroom Description
            </label>
            <textarea
              name="description"
              className="fuel-input"
              value={formData.description}
              onChange={handleChange}
              placeholder="Ka-Chow! Speed. I am speed."
              style={{ minHeight: '80px', resize: 'vertical' }}
            />
          </div>

          {/* Form Actions */}
          <div style={{ display: 'flex', gap: '15px' }}>
            <Link
              to="/"
              style={{
                flex: 1,
                background: 'transparent',
                color: 'var(--text-cream)',
                border: '1px solid var(--text-muted)',
                padding: '12px',
                textAlign: 'center',
                textDecoration: 'none',
                fontFamily: 'Russo One',
                textTransform: 'uppercase',
                borderRadius: 'var(--border-radius-sm)',
                fontSize: '14px',
                cursor: 'pointer'
              }}
            >
              Cancel
            </Link>

            <button
              type="submit"
              className="nitro-btn"
              disabled={submitting}
              style={{ flex: 2, padding: '12px' }}
            >
              {submitting ? (
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <div className="spinning-tire" style={{ width: '16px', height: '16px', borderWidth: '2.5px' }} />
                  <span>Saving...</span>
                </div>
              ) : (
                buttonText
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
