-- Seed default admin user (password: Admin@123, bcrypt-hashed)
INSERT INTO users (email, password, name, role)
VALUES ('admin@cardealership.com', '$2a$10$X5tE5u1fD0iZ7Ym3tJ/2E.s6pLszD4cskC5XoT6a8K2t4o9Jm2y/q', 'Admin', 'ADMIN');
