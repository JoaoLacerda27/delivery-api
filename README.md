# Delivery API

Order and Delivery Management API built with Spring Boot.

## 🚀 Quick Start

### Prerequisites

- Java 21
- Maven
- Docker and Docker Compose

### 🐳 Option 1: Run Everything with Docker Compose (Recommended)

This will start PostgreSQL, MongoDB, and the application all together:

```bash
# Build and start all services
docker-compose up -d --build

# View logs
docker-compose logs -f app

# Stop everything
docker-compose down
```

This will start:
- PostgreSQL on port `5432`
- MongoDB on port `27017`
- Application on port `8080`

### 💻 Option 2: Run Locally (Development)

#### 1️⃣ Start Infrastructure (PostgreSQL + MongoDB)

```bash
docker-compose up -d postgres mongodb
```

#### 2️⃣ Configure Environment Variables

Copy `env.example` to `.env` and adjust if needed:

```bash
# On Windows (PowerShell)
Copy-Item env.example .env

# On Linux/Mac
cp env.example .env
```

The default values should work if you're using the docker-compose setup.

#### 3️⃣ Run the Application

```bash
# Using Maven Wrapper
./mvnw spring-boot:run

# Or using the VS Code task
# Press Ctrl+Shift+P -> "Delivery API" > "Backend"
```

### 4️⃣ Access the API

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/v3/api-docs

## 📁 Project Structure

```
com.company.deliveryapi
 ├── application
 ├── domain
 │    ├── postgres
 │    └── mongo
 ├── repository
 │    ├── postgres
 │    └── mongo
 ├── integration
 └── shared
      ├── security
      ├── config
      └── exception
```

## 🛠️ Technology Stack

- **Spring Boot 3.5.9**
- **PostgreSQL** - Business data (CRUD)
- **MongoDB** - Cache/snapshot of external APIs
- **Spring Security** - OAuth2 + JWT
- **Springdoc OpenAPI** - Swagger documentation

## 🔧 Configuration

### Database Connections

The application uses environment variables for database configuration:

- `POSTGRES_URL` - PostgreSQL connection URL
- `POSTGRES_USER` - PostgreSQL username
- `POSTGRES_PASSWORD` - PostgreSQL password
- `MONGO_URI` - MongoDB connection URI
- `OAUTH_ISSUER_URI` - OAuth2 JWT issuer URI

### Docker Compose

To stop the containers:

```bash
docker-compose down
```

To stop and remove volumes (⚠️ deletes data):

```bash
docker-compose down -v
```

## 📝 API Documentation

Once the application is running, access the Swagger UI at:
http://localhost:8080/swagger-ui.html

## 🔐 Security

The API is secured with OAuth2 + JWT. Swagger endpoints are publicly accessible for development.

## 🧪 Testing

```bash
./mvnw test
```

## 📦 Building

```bash
./mvnw clean package
```

## 🐳 Docker

### Build the Docker Image

```bash
docker build -t delivery-api:latest .
```

### Run the Docker Container

Make sure PostgreSQL and MongoDB are running (via docker-compose):

```bash
# Start infrastructure
docker-compose up -d

# Run the application container
docker run -d \
  --name delivery-api \
  -p 8080:8080 \
  --env-file .env \
  --network host \
  delivery-api:latest
```

Or use docker-compose to run everything together (you can add the app service to docker-compose.yml):

```bash
# Build and run
docker-compose up -d --build
```

### Stop the Container

```bash
docker stop delivery-api
docker rm delivery-api
```

