-- Add deleted column to orders table for soft delete
ALTER TABLE orders ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;

-- Create index for better query performance
CREATE INDEX IF NOT EXISTS idx_orders_deleted ON orders(deleted);


