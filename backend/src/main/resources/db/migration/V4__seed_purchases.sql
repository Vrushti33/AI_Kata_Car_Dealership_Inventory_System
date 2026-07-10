-- Seed initial purchase history (linked to users and vehicles seeded in V2 and V3)
-- We look up user and vehicle IDs using subqueries to guarantee ID safety

INSERT INTO purchases (user_id, vehicle_id, quantity, total_price, purchased_at) VALUES 
(
  (SELECT id FROM users WHERE email = 'mater_fan@radiator.com'), 
  (SELECT id FROM vehicles WHERE model = 'Tow Truck Deluxe' LIMIT 1), 
  1, 32000.00, CURRENT_TIMESTAMP - INTERVAL '5 days'
),
(
  (SELECT id FROM users WHERE email = 'sally_carrera@route66.com'), 
  (SELECT id FROM vehicles WHERE model = 'McQueen Special' LIMIT 1), 
  1, 95000.00, CURRENT_TIMESTAMP - INTERVAL '3 days'
),
(
  (SELECT id FROM users WHERE email = 'doc_hudson_legacy@classic.com'), 
  (SELECT id FROM vehicles WHERE model = 'Hornet Classic' LIMIT 1), 
  1, 45000.00, CURRENT_TIMESTAMP - INTERVAL '1 day'
),
(
  (SELECT id FROM users WHERE email = 'mater_fan@radiator.com'), 
  (SELECT id FROM vehicles WHERE model = 'Fiat Pit Stop' LIMIT 1), 
  2, 44000.00, CURRENT_TIMESTAMP - INTERVAL '12 hours'
);
