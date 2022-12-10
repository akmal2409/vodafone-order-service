-- Supports the constraint of only one tuple of (email, product_id)
CREATE TABLE IF NOT EXISTS orders (
    id serial PRIMARY KEY,
    email VARCHAR(64) NOT NULL,
    product_id VARCHAR(128) NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    UNIQUE (email, product_id)
);
