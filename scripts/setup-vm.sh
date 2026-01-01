#!/bin/bash

# Script de Setup Inicial da VM no GCP
# Execute este script na primeira vez que configurar a VM

set -e

echo "🚀 Configurando VM para Delivery API..."

# Atualizar sistema
echo "📦 Atualizando sistema..."
sudo apt-get update
sudo apt-get upgrade -y

# Instalar Java 21
echo "☕ Instalando Java 21..."
sudo apt-get install -y openjdk-21-jdk

# Verificar instalação
java -version

# Instalar Maven
echo "🔨 Instalando Maven..."
sudo apt-get install -y maven

# Verificar instalação
mvn -version

# Instalar Docker (opcional, se quiser usar containers)
echo "🐳 Instalando Docker..."
sudo apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER

# Instalar Git
echo "📥 Instalando Git..."
sudo apt-get install -y git

# Criar diretório da aplicação
echo "📁 Criando diretório da aplicação..."
sudo mkdir -p /opt/delivery-api
sudo chown $USER:$USER /opt/delivery-api

# Instalar PostgreSQL (se não usar Cloud SQL)
echo "🗄️ Instalando PostgreSQL..."
sudo apt-get install -y postgresql postgresql-contrib

# Instalar MongoDB (se não usar MongoDB Atlas)
echo "🍃 Instalando MongoDB..."
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
sudo apt-get update
sudo apt-get install -y mongodb-org

# Iniciar serviços
sudo systemctl start postgresql
sudo systemctl enable postgresql
sudo systemctl start mongod
sudo systemctl enable mongod

# Configurar firewall (se necessário)
echo "🔥 Configurando firewall..."
sudo ufw allow 22/tcp   # SSH
sudo ufw allow 8080/tcp # API
sudo ufw allow 5432/tcp # PostgreSQL (se necessário)
sudo ufw allow 27017/tcp # MongoDB (se necessário)
sudo ufw --force enable

echo "✅ Setup concluído!"
echo ""
echo "📝 Próximos passos:"
echo "1. Configure o banco de dados PostgreSQL"
echo "2. Configure o MongoDB"
echo "3. Execute o script de deploy: ./scripts/deploy.sh"

