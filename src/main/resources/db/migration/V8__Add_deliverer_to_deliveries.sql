-- Add deliverer_name column to deliveries table
ALTER TABLE deliveries ADD COLUMN IF NOT EXISTS deliverer_name VARCHAR(255);

-- Create index for better query performance
CREATE INDEX IF NOT EXISTS idx_deliveries_deliverer_name ON deliveries(deliverer_name);

