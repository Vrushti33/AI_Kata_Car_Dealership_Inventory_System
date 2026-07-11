import { useState, useEffect } from 'react';

/**
 * Custom hook to debounce rapid state updates (e.g. search inputs)
 * @param {*} value 
 * @param {Number} delay 
 * @returns {*}
 */
export default function useDebounce(value, delay) {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}
