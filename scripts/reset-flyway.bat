@echo off
REM Script para resetar o histórico do Flyway no Windows
echo DROP TABLE IF EXISTS flyway_schema_history CASCADE; | docker exec -i delivery-postgres psql -U postgres -d delivery
echo Histórico do Flyway limpo! Agora rode a aplicacao novamente.




