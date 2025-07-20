CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear índices para mejor performance
CREATE INDEX IF NOT EXISTS idx_orders_customer_email ON orders(customer_email);
CREATE INDEX IF NOT EXISTS idx_orders_stripe_session_id ON orders(stripe_session_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_notifications_order_id ON notifications(order_id);
CREATE INDEX IF NOT EXISTS idx_notifications_type_status ON notifications(type, status);

-- Insertar datos de prueba adicionales
INSERT INTO products (id, name, description, price, emoji) VALUES
                                                               ('tablet_001', 'iPad Pro 12.9"', 'Tablet profesional con chip M2, ideal para creativos', 1299.00, '📱'),
                                                               ('watch_001', 'Apple Watch Ultra', 'Smartwatch resistente para deportes extremos', 799.00, '⌚'),
                                                               ('keyboard_001', 'Magic Keyboard', 'Teclado inalámbrico premium con retroiluminación', 199.00, '⌨️'),
                                                               ('mouse_001', 'Magic Mouse', 'Mouse inalámbrico con superficie Multi-Touch', 99.00, '🖱️'),
                                                               ('charger_001', 'MagSafe Charger', 'Cargador inalámbrico magnético rápido', 39.00, '🔌'),
                                                               ('case_001', 'iPhone Leather Case', 'Funda de cuero premium para iPhone', 59.00, '📱')
ON CONFLICT (id) DO NOTHING;