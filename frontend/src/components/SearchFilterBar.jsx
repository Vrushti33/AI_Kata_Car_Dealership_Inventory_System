import React from 'react';

const CATEGORIES = ['SEDAN', 'SUV', 'TRUCK', 'COUPE', 'CONVERTIBLE', 'HATCHBACK', 'VAN', 'ELECTRIC'];

export default function SearchFilterBar({
  search,
  setSearch,
  category,
  setCategory,
  minPrice,
  setMinPrice,
  maxPrice,
  setMaxPrice
}) {
  const handleClear = () => {
    setSearch('');
    setCategory('');
    setMinPrice('');
    setMaxPrice('');
  };

  return (
    <div style={{
      background: 'var(--surface-dark)',
      border: '1.5px solid var(--secondary-orange)',
      borderRadius: 'var(--border-radius-md)',
      padding: '20px',
      marginBottom: '35px',
      boxShadow: '0 4px 15px rgba(0,0,0,0.2)'
    }}>
      <h3 className="brand-font glow-yellow" style={{ color: 'var(--accent-yellow)', fontSize: '16px', marginBottom: '15px' }}>
        Console (Filters)
      </h3>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
        gap: '15px',
        alignItems: 'end'
      }}>
        {/* Search Make/Model */}
        <div>
          <label id="search-make-model-label" style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)' }}>
            Search Make/Model
          </label>
          <input
            type="text"
            className="fuel-input"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="e.g. Lightning"
            style={{ padding: '10px' }}
            aria-labelledby="search-make-model-label"
          />
        </div>

        {/* Category select */}
        <div>
          <label id="search-category-label" style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)' }}>
            Category
          </label>
          <select
            className="fuel-input"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            style={{ padding: '10px', cursor: 'pointer' }}
            aria-labelledby="search-category-label"
          >
            <option value="">All Categories</option>
            {CATEGORIES.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>
        </div>

        {/* Min Price */}
        <div>
          <label id="search-min-price-label" style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)' }}>
            Min Price ($)
          </label>
          <input
            type="number"
            className="fuel-input"
            value={minPrice}
            onChange={(e) => setMinPrice(e.target.value)}
            placeholder="0"
            style={{ padding: '10px' }}
            min="0"
            aria-labelledby="search-min-price-label"
          />
        </div>

        {/* Max Price */}
        <div>
          <label id="search-max-price-label" style={{ fontSize: '11px', fontFamily: 'Russo One', color: 'var(--text-cream)' }}>
            Max Price ($)
          </label>
          <input
            type="number"
            className="fuel-input"
            value={maxPrice}
            onChange={(e) => setMaxPrice(e.target.value)}
            placeholder="Any"
            style={{ padding: '10px' }}
            min="0"
            aria-labelledby="search-max-price-label"
          />
        </div>

        {/* Clear Filters Button */}
        <div>
          <button
            onClick={handleClear}
            style={{
              width: '100%',
              background: 'transparent',
              color: 'var(--text-cream)',
              border: '1px solid var(--text-muted)',
              padding: '11px',
              fontFamily: 'Russo One',
              fontSize: '13px',
              textTransform: 'uppercase',
              borderRadius: 'var(--border-radius-sm)',
              cursor: 'pointer',
              transition: 'all 0.2s'
            }}
            onMouseEnter={(e) => {
              e.target.style.borderColor = 'var(--primary-red)';
              e.target.style.color = 'var(--primary-red)';
            }}
            onMouseLeave={(e) => {
              e.target.style.borderColor = 'var(--text-muted)';
              e.target.style.color = 'var(--text-cream)';
            }}
          >
            Reset Board
          </button>
        </div>
      </div>
    </div>
  );
}
