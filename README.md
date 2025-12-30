# 📦 Delivery API

## 📖 Project Overview

The **Delivery API** is a RESTful backend application built with **Java 21** and **Spring Boot**, designed to manage deliveries and orders while integrating with external services.

The application demonstrates modern backend development practices, including:

- Clean architecture with feature-based modularization
- Integration with both **PostgreSQL** and **MongoDB**
- External REST API consumption
- Secure authentication using **OAuth2 with JWT**
- API documentation using **Swagger / OpenAPI**
- Containerized infrastructure using **Docker**

This project was developed as part of a technical assessment to showcase backend engineering skills.

---

## 🛠 Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Security (OAuth2 Resource Server)**
- **PostgreSQL** (relational data / CRUD)
- **MongoDB** (external service data & events)
- **WebClient** (external API integration)
- **Swagger / OpenAPI**
- **Docker & Docker Compose**
- **Maven**

---

## 🏗 Architecture

### Feature-based Architecture

The project follows a **feature-based modular architecture**:

```
application/
  ├── delivery/        # Delivery feature module
  ├── order/           # Order feature module
  └── integrations/   # External API integrations
      └── viacep/      # ViaCEP integration

shared/
  ├── config/          # Shared configurations
  ├── security/        # Security configuration
  ├── exception/       # Global exception handling
  └── model/           # Shared domain models
```

**Key principles:**
- Feature-based architecture
- Shared concerns (security, config, exceptions) centralized under `shared`

---

## 🗄 Database Setup

### PostgreSQL (CRUD Data)

Used to store core business entities such as:
- **Orders**
- **Deliveries**

**Local setup (Docker):**
```bash
docker-compose up -d postgres
```

### MongoDB (External & Tracking Data)

Used to store:
- Delivery tracking events
- Data retrieved from external APIs

**Local setup (Docker):**
```bash
docker-compose up -d mongodb
```

---

## 🌐 External API Integration

The application integrates with a public external REST API to enrich delivery data.

**Example:**
- **ViaCEP API** for address resolution based on ZIP code

**Key points:**
- Implemented using **WebClient**
- External data is stored in **MongoDB**
- Data is combined with PostgreSQL entities in API responses when requested

---

## 🔄 Business Logic

- **CRUD operations** are handled via PostgreSQL
- **External service data** is stored in MongoDB
- A GET endpoint with a specific parameter triggers:
  - External API call (if data is not cached)
  - Combination of PostgreSQL + MongoDB data in the response

**Example:**
```
GET /api/deliveries/{id}?includeTracking=true
```

---

## 🔐 Security & Authentication

The API is secured using **OAuth2 with JWT**.

### Authentication Model

- Authentication is handled by an external OAuth provider (e.g., Auth0 or Google)
- The backend acts as a **Resource Server**
- No user credentials or login logic are stored in the backend

### Configuration (application.yml)

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${OAUTH_ISSUER_URI}
```

### Environment Variables

Create a `.env` file based on `.env.example` and provide the required values.

**Development mode:**
- Set `SECURITY_ENABLED=false` to disable authentication (for development)
- Set `SECURITY_ENABLED=true` to enable OAuth2 JWT authentication (for production)

---

## 📘 API Documentation (Swagger)

**Swagger UI** is available at:
```
http://localhost:8080/swagger-ui.html
```

**Features:**
- JWT authentication support via **Authorize** button
- Full documentation of endpoints, requests, and responses
- Interactive API testing

---

## ▶️ Running the Application

### Prerequisites

- **Java 21**
- **Docker & Docker Compose**
- **Maven**

### Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd delivery-api
   ```

2. **Create a `.env` file** based on `.env.example`
   ```bash
   cp env.example .env
   ```

3. **Start the databases:**
   ```bash
   docker-compose up -d postgres mongodb
   ```

4. **Build the application:**
   ```bash
   ./mvnw clean package
   ```

5. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Or using the JAR:
   ```bash
   java -jar target/delivery-api-0.0.1-SNAPSHOT.jar
   ```

### 🐳 Option: Run Everything with Docker Compose

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

---

## 🧪 Testing

- Endpoints can be tested via **Swagger UI**
- Authentication requires a valid JWT from the configured OAuth provider
- Standard HTTP status codes and validation errors are returned

### Example API Calls

**Create an Order:**
```bash
POST /api/orders
Content-Type: application/json

{
  "customerName": "John Doe",
  "totalAmount": 99.99
}
```

**Create a Delivery:**
```bash
POST /api/deliveries/{orderId}
Content-Type: application/json

{
  "street": "Main Street",
  "city": "São Paulo",
  "state": "SP",
  "zipCode": "01001000"
}
```

**Get Delivery with Tracking:**
```bash
GET /api/deliveries/{deliveryId}?includeTracking=true
```

---

## 📁 Project Structure

```
com.company.delivery_api
 ├── application
 │    ├── delivery
 │    │    ├── controller
 │    │    │    ├── doc
 │    │    │    │    └── DeliveryDoc.java
 │    │    │    └── DeliveryController.java
 │    │    ├── domain
 │    │    │    ├── mongo
 │    │    │    │    ├── AddressInfo.java
 │    │    │    │    └── DeliveryEvent.java
 │    │    │    └── postgres
 │    │    │        ├── Delivery.java
 │    │    │        └── enums
 │    │    │            └── DeliveryStatusEnum.java
 │    │    ├── dto
 │    │    │    ├── CreateDeliveryRequest.java
 │    │    │    ├── DeliveryResponse.java
 │    │    │    ├── DeliveryWithTrackingResponse.java
 │    │    │    ├── TrackingEventResponse.java
 │    │    │    └── UpdateDeliveryStatusRequest.java
 │    │    ├── repository
 │    │    │    ├── mongo
 │    │    │    │    ├── AddressInfoRepository.java
 │    │    │    │    └── DeliveryEventRepository.java
 │    │    │    └── postgres
 │    │    │        └── DeliveryRepository.java
 │    │    └── service
 │    │        ├── DeliveryQueryService.java
 │    │        └── DeliveryService.java
 │    ├── integrations
 │    │    └── viacep
 │    │        ├── client
 │    │        │    └── ViaCepClient.java
 │    │        └── dto
 │    │            └── ViaCepResponse.java
 │    └── order
 │        ├── controller
 │        │    ├── doc
 │        │    │    └── OrderDoc.java
 │        │    └── OrderController.java
 │        ├── domain
 │        │    └── postgres
 │        │        └── Order.java
 │        ├── dto
 │        │    ├── CreateOrderRequest.java
 │        │    └── OrderResponse.java
 │        ├── repository
 │        │    └── postgres
 │        │        └── OrderRepository.java
 │        └── service
 │            └── OrderService.java
 └── shared
      ├── config
      │    ├── SwaggerConfig.java
      │    └── WebClientConfig.java
      ├── exception
      │    ├── response
      │    │    └── ErrorResponse.java
      │    ├── types
      │    │    ├── DeliveryAlreadyExistsException.java
      │    │    ├── DeliveryNotFoundException.java
      │    │    ├── InvalidDeliveryStatusTransitionException.java
      │    │    └── OrderNotFoundException.java
      │    └── GlobalExceptionHandler.java
      ├── model
      │    └── ModelBase.java
      └── security
           ├── JwtAuthConverter.java
           └── SecurityConfig.java
```

---

## ⚠️ Known Issues / Limitations

- External API availability depends on third-party service uptime
- Token generation is delegated to the OAuth provider and not handled internally

---

## 🐳 Docker Support

The application can be containerized using the provided `Dockerfile`.

### Build image
```bash
docker build -t delivery-api .
```

### Run container
```bash
docker run -p 8080:8080 delivery-api
```

### Docker Compose

The `docker-compose.yml` file includes:
- PostgreSQL service
- MongoDB service
- Application service (with build configuration)

---

## 📝 Environment Variables

See `.env.example` for all required environment variables:

```bash
# PostgreSQL Configuration
POSTGRES_URL=jdbc:postgresql://localhost:5432/delivery
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# MongoDB Configuration
MONGO_URI=mongodb://localhost:27017/delivery

# OAuth2 Configuration
OAUTH_ISSUER_URI=https://your-tenant.us.auth0.com/

# Security Configuration
SECURITY_ENABLED=false
```

---

## 📄 License

This project is licensed under the Apache 2.0 License.

---

## 👥 Author

Developed as part of a technical assessment to demonstrate backend engineering skills.
