-- Rename deliverer_email column to deliverer_name
ALTER TABLE deliveries RENAME COLUMN deliverer_email TO deliverer_name;

-- Drop old index and create new one
DROP INDEX IF EXISTS idx_deliveries_deliverer_email;
CREATE INDEX IF NOT EXISTS idx_deliveries_deliverer_name ON deliveries(deliverer_name);


