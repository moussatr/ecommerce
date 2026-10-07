INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Montre connectée', 'montre-connectee',
       'Montre intelligente avec suivi d’activité et notifications.', 149.90, 14, TRUE, id
FROM categories WHERE slug = 'electronique'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Enceinte Bluetooth', 'enceinte-bluetooth',
       'Enceinte portable compacte avec son immersif et autonomie 12h.', 79.00, 18, TRUE, id
FROM categories WHERE slug = 'electronique'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Chaise de bureau ergonomique', 'chaise-de-bureau-ergonomique',
       'Chaise confortable avec appui lombaire réglable et assise renforcée.', 219.00, 7, TRUE, id
FROM categories WHERE slug = 'maison'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Coussin design', 'coussin-design',
       'Coussin décoratif en matière douce pour canapé ou lit.', 29.90, 25, TRUE, id
FROM categories WHERE slug = 'maison'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Trousse de voyage', 'trousse-de-voyage',
       'Trousse organisée avec compartiments imperméables.', 42.50, 16, TRUE, id
FROM categories WHERE slug = 'accessoires'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Chargeur USB-C', 'chargeur-usb-c',
       'Chargeur rapide 65W compatible smartphone, tablette et ordinateur portable.', 35.00, 30, TRUE, id
FROM categories WHERE slug = 'accessoires'
ON CONFLICT (slug) DO NOTHING;
