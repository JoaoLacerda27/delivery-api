#!/bin/bash
# Script para resetar o histórico do Flyway
# Execute: docker exec -i delivery-postgres psql -U postgres -d delivery < scripts/reset-flyway.sql

echo "DROP TABLE IF EXISTS flyway_schema_history CASCADE;" | docker exec -i delivery-postgres psql -U postgres -d delivery
echo "Histórico do Flyway limpo! Agora rode a aplicação novamente."




