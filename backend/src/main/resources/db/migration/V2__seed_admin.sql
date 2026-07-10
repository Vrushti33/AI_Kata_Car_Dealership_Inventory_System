-- Seed default admin user (password: Admin@123, bcrypt-hashed)
INSERT INTO users (email, password, name, role)
VALUES ('admin@cardealership.com', '$2a$10$X5tE5u1fD0iZ7Ym3tJ/2E.s6pLszD4cskC5XoT6a8K2t4o9Jm2y/q', 'Admin', 'ADMIN');

-- Seed standard users (password: User@123, bcrypt-hashed)
INSERT INTO users (email, password, name, role) VALUES
('mater_fan@radiator.com', '$2a$10$tZptFv0Wep6MhJbeB6WJWe0.yYk08/OqIom.C0c92KSw59Z0Z7b42', 'Tow Mater Fan', 'USER'),
('sally_carrera@route66.com', '$2a$10$tZptFv0Wep6MhJbeB6WJWe0.yYk08/OqIom.C0c92KSw59Z0Z7b42', 'Sally Carrera', 'USER'),
('doc_hudson_legacy@classic.com', '$2a$10$tZptFv0Wep6MhJbeB6WJWe0.yYk08/OqIom.C0c92KSw59Z0Z7b42', 'Hornet Fan', 'USER');
