/**
 * Helper to determine if a user has admin permissions
 * @param {Object} user 
 * @returns {Boolean}
 */
export const isAdmin = (user) => {
  return user?.role === 'ADMIN';
};
