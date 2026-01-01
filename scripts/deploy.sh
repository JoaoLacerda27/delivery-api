#!/bin/bash

# Script de Deploy da Delivery API na VM do GCP
# Execute este script para fazer deploy da aplicação

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configurações
APP_DIR="/opt/delivery-api"
SERVICE_NAME="delivery-api"
JAR_NAME="delivery-api-0.0.1-SNAPSHOT.jar"

echo -e "${GREEN}🚀 Iniciando deploy da Delivery API...${NC}"

# Verificar se está no diretório do projeto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Erro: Execute este script a partir do diretório raiz do projeto!${NC}"
    exit 1
fi

# Verificar Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java não encontrado. Execute primeiro: ./scripts/setup-vm.sh${NC}"
    exit 1
fi

# Verificar Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven não encontrado. Execute primeiro: ./scripts/setup-vm.sh${NC}"
    exit 1
fi

# Parar serviço se estiver rodando
echo -e "${YELLOW}⏸️  Parando serviço existente...${NC}"
sudo systemctl stop $SERVICE_NAME 2>/dev/null || true

# Criar diretório da aplicação
echo -e "${YELLOW}📁 Criando diretório da aplicação...${NC}"
sudo mkdir -p $APP_DIR
sudo chown $USER:$USER $APP_DIR

# Build da aplicação
echo -e "${YELLOW}🔨 Compilando aplicação...${NC}"
mvn clean package -DskipTests

# Copiar JAR
echo -e "${YELLOW}📦 Copiando JAR...${NC}"
cp target/$JAR_NAME $APP_DIR/app.jar

# Criar arquivo de ambiente (se não existir)
if [ ! -f "$APP_DIR/.env" ]; then
    echo -e "${YELLOW}📝 Criando arquivo .env...${NC}"
    cat > $APP_DIR/.env << EOF
# PostgreSQL Configuration
POSTGRES_URL=jdbc:postgresql://localhost:5432/delivery
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# MongoDB Configuration
MONGO_URI=mongodb://localhost:27017/delivery

# OAuth2 Configuration
OAUTH_ISSUER_URI=https://your-tenant.us.auth0.com/
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GOOGLE_REDIRECT_URI=http://SEU-IP:8080/login/oauth2/code/google

# Security Configuration
SECURITY_ENABLED=false

# Server Configuration
PORT=8080

# Frontend Configuration
FRONTEND_URL=http://localhost:5173
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000
EOF
    echo -e "${YELLOW}⚠️  Configure o arquivo $APP_DIR/.env com suas credenciais!${NC}"
fi

# Criar serviço systemd
echo -e "${YELLOW}⚙️  Configurando serviço systemd...${NC}"
sudo tee /etc/systemd/system/$SERVICE_NAME.service > /dev/null << EOF
[Unit]
Description=Delivery API Service
After=network.target postgresql.service mongod.service

[Service]
Type=simple
User=$USER
WorkingDirectory=$APP_DIR
EnvironmentFile=$APP_DIR/.env
ExecStart=/usr/bin/java -jar $APP_DIR/app.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF

# Recarregar systemd
sudo systemctl daemon-reload

# Habilitar serviço
sudo systemctl enable $SERVICE_NAME

# Iniciar serviço
echo -e "${YELLOW}▶️  Iniciando serviço...${NC}"
sudo systemctl start $SERVICE_NAME

# Aguardar alguns segundos
sleep 5

# Verificar status
if sudo systemctl is-active --quiet $SERVICE_NAME; then
    echo -e "${GREEN}✅ Deploy concluído com sucesso!${NC}"
    echo ""
    echo -e "${GREEN}📊 Status do serviço:${NC}"
    sudo systemctl status $SERVICE_NAME --no-pager -l
    echo ""
    echo -e "${GREEN}📝 Comandos úteis:${NC}"
    echo "  Ver logs: sudo journalctl -u $SERVICE_NAME -f"
    echo "  Parar:    sudo systemctl stop $SERVICE_NAME"
    echo "  Iniciar:  sudo systemctl start $SERVICE_NAME"
    echo "  Reiniciar: sudo systemctl restart $SERVICE_NAME"
    echo "  Status:   sudo systemctl status $SERVICE_NAME"
else
    echo -e "${RED}❌ Erro ao iniciar serviço!${NC}"
    echo -e "${YELLOW}Verifique os logs: sudo journalctl -u $SERVICE_NAME -n 50${NC}"
    exit 1
fi

