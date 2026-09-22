INSERT INTO categories (name, slug, active)
VALUES
    ('Électronique', 'electronique', TRUE),
    ('Maison', 'maison', TRUE),
    ('Accessoires', 'accessoires', TRUE)
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Casque audio sans fil', 'casque-audio-sans-fil',
       'Casque confortable avec réduction de bruit.', 89.90, 12, TRUE, id
FROM categories WHERE slug = 'electronique'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Lampe de bureau', 'lampe-de-bureau',
       'Lampe LED orientable pour le bureau ou la lecture.', 34.50, 20, TRUE, id
FROM categories WHERE slug = 'maison'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, price, stock, active, category_id)
SELECT 'Sac à dos urbain', 'sac-a-dos-urbain',
       'Sac compact avec compartiment pour ordinateur portable.', 59.00, 8, TRUE, id
FROM categories WHERE slug = 'accessoires'
ON CONFLICT (slug) DO NOTHING;