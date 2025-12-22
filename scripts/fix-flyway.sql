-- Script para corrigir o histórico do Flyway
-- Execute: docker exec -i delivery-postgres psql -U postgres -d delivery < scripts/fix-flyway.sql

-- Opção 1: Limpar o histórico e reaplicar (⚠️ apaga histórico de migrações)
DROP TABLE IF EXISTS flyway_schema_history CASCADE;

-- Opção 2: Atualizar o checksum da migração V1 manualmente (descomente se preferir)
-- UPDATE flyway_schema_history 
-- SET checksum = -1967293681 
-- WHERE version = '1';

-- Verificar o histórico
SELECT * FROM flyway_schema_history;

