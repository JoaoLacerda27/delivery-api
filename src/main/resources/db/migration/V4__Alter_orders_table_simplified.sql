-- Remove foreign key constraint and old columns
ALTER TABLE orders DROP CONSTRAINT IF EXISTS FK_ORDER__CUSTOMER_ID;
ALTER TABLE orders DROP CONSTRAINT IF EXISTS chk_orders_status;

-- Drop old columns (PostgreSQL doesn't support IF EXISTS for columns, so we check first)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'customer_id') THEN
        ALTER TABLE orders DROP COLUMN customer_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'product_id') THEN
        ALTER TABLE orders DROP COLUMN product_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'quantity') THEN
        ALTER TABLE orders DROP COLUMN quantity;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'status') THEN
        ALTER TABLE orders DROP COLUMN status;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'updated_at') THEN
        ALTER TABLE orders DROP COLUMN updated_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'created_by') THEN
        ALTER TABLE orders DROP COLUMN created_by;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'updated_by') THEN
        ALTER TABLE orders DROP COLUMN updated_by;
    END IF;
END $$;

-- Add new columns
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'customer_name') THEN
        ALTER TABLE orders ADD COLUMN customer_name VARCHAR(255) NOT NULL DEFAULT 'Unknown';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'orders' AND column_name = 'total_amount') THEN
        ALTER TABLE orders ADD COLUMN total_amount NUMERIC(19, 2) NOT NULL DEFAULT 0;
    END IF;
END $$;

-- Drop old indexes
DROP INDEX IF EXISTS idx_orders_customer_id;
DROP INDEX IF EXISTS idx_orders_status;
DROP INDEX IF EXISTS idx_orders_product_id;

-- Create new indexes
CREATE INDEX IF NOT EXISTS idx_orders_customer_name ON orders(customer_name);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);

