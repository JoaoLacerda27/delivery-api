CREATE TABLE IF NOT EXISTS customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    CONSTRAINT customers_email_unique UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_customers_email ON customers(email);

