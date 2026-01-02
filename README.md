# Delivery API

## Project Overview

The **Delivery API** is a RESTful backend application built with **Java 21** and **Spring Boot 3.3.5**, designed to manage deliveries and orders while integrating with external services.

This application demonstrates modern backend development practices, including:

- Integration with external REST API (ViaCEP)
- CRUD operations stored in PostgreSQL
- External service data stored in MongoDB
- OAuth2 authentication with Google
- API documentation using Swagger/OpenAPI
- Docker containerization support

This project was developed as part of the ZCAM technical assessment for Java Backend Developer position.

---

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **Docker** and **Docker Compose** (for local database setup)
- **PostgreSQL** (local or cloud service like Cloud SQL)
- **MongoDB** (local or cloud service like MongoDB Atlas)
- **Google OAuth2 credentials** (Client ID and Client Secret)

---

## Project Structure

The project follows a standard Spring Boot structure with clear separation of concerns:

```
src/main/java/com/company/delivery_api/
├── application/
│   ├── delivery/          # Delivery feature module
│   │   ├── controller/    # REST controllers
│   │   ├── domain/        # Domain models (PostgreSQL and MongoDB)
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── repository/    # Data access layer
│   │   └── service/       # Business logic
│   ├── order/             # Order feature module
│   │   ├── controller/
│   │   ├── domain/
│   │   ├── dto/
│   │   ├── repository/
│   │   └── service/
│   ├── integrations/      # External API integrations
│   │   └── viacep/        # ViaCEP API integration
│   └── auth/              # Authentication module
│       ├── controller/
│       ├── dto/
│       └── service/
└── shared/
    ├── config/            # Shared configurations
    ├── security/          # Security configuration
    ├── exception/         # Global exception handling
    └── model/             # Shared domain models
```

**Key principles:**
- Feature-based modular architecture
- Separation of concerns (controllers, services, repositories)
- Shared concerns centralized under `shared` package

---

## Configuration Files

### application.yml

The main configuration file is located at `src/main/resources/application.yaml` and contains:

- Database configurations (PostgreSQL and MongoDB)
- OAuth2 client configuration
- Server port configuration
- Swagger/OpenAPI configuration
- External API endpoints

### Environment Variables

Sensitive information is configured via environment variables. See `.env.example` for all required variables.

---

## Database Setup

### NoSQL Database (MongoDB)

MongoDB is used to store all data related to external services (e.g., address information from ViaCEP API, delivery tracking events).

#### Local Setup (Docker)

```bash
docker-compose up -d mongodb
```

#### Cloud Setup (MongoDB Atlas)

1. Create a free account at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a cluster (M0 free tier is sufficient)
3. Configure Network Access: Add `0.0.0.0/0` or your specific IP
4. Create a database user
5. Get the connection string: `mongodb+srv://user:password@cluster.mongodb.net/delivery`

### SQL Database (PostgreSQL)

PostgreSQL is used to store all data related to CRUD operations (orders, deliveries, customers).

#### Local Setup (Docker)

```bash
docker-compose up -d postgres
```

#### Cloud Setup (Cloud SQL)

1. Create a PostgreSQL instance in Google Cloud SQL
2. Create database: `delivery`
3. Create user with appropriate permissions
4. Get connection string: `jdbc:postgresql://IP:5432/delivery`

### Database Models

- **PostgreSQL Models**: Located in `application/*/domain/postgres/`
  - `Order.java` - Order entity
  - `Delivery.java` - Delivery entity
  - `Customer.java` - Customer entity

- **MongoDB Models**: Located in `application/*/domain/mongo/`
  - `AddressInfo.java` - Address information from external API
  - `DeliveryEvent.java` - Delivery tracking events

### Data Validation

All mandatory attributes are validated using Spring's validation framework (`@NotNull`, `@NotBlank`, `@Valid`, etc.). Meaningful error messages are returned when validation fails.

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd delivery-api
```

### 2. Configure Environment Variables

Create a `.env` file based on `.env.example`:

```bash
cp .env.example .env
```

Edit `.env` and provide the required values:

```bash
# PostgreSQL Configuration
POSTGRES_URL=jdbc:postgresql://localhost:5432/delivery
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# MongoDB Configuration
MONGO_URI=mongodb://localhost:27017/delivery
# OR for MongoDB Atlas:
# MONGO_URI=mongodb+srv://user:password@cluster.mongodb.net/delivery

# OAuth2 Configuration
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google
OAUTH_ISSUER_URI=https://your-tenant.us.auth0.com/

# Security Configuration
SECURITY_ENABLED=false  # Set to true for production

# Server Configuration
PORT=8080

# Frontend Configuration
FRONTEND_URL=http://localhost:5173
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000
```

### 3. Start Databases

Using Docker Compose:

```bash
docker-compose up -d postgres mongodb
```

Or start them manually if you have PostgreSQL and MongoDB installed locally.

### 4. Build the Application

```bash
./mvnw clean package
```

Or using Maven directly:

```bash
mvn clean package
```

---

## Running the Application

### Option 1: Using Maven

```bash
./mvnw spring-boot:run
```

### Option 2: Using JAR

```bash
java -jar target/delivery-api-0.0.1-SNAPSHOT.jar
```

### Option 3: Using Docker Compose

This will start PostgreSQL, MongoDB, and the application all together:

```bash
# Build and start all services
docker-compose up -d --build

# View logs
docker-compose logs -f app

# Stop everything
docker-compose down
```

The application will be available at:
- **API Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

---

## API Documentation

### Swagger UI

The API documentation is available via Swagger UI at:

**http://localhost:8080/swagger-ui.html**

Swagger UI provides:
- Interactive API testing
- Complete endpoint documentation
- Request/response schemas
- OAuth2 authentication support via "Authorize" button

### API Endpoints

#### Orders

- `GET /api/orders` - List all orders (paginated)
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create a new order
- `PATCH /api/orders/{id}` - Update an order
- `DELETE /api/orders/{id}` - Delete an order

#### Deliveries

- `GET /api/deliveries` - List all deliveries (paginated)
- `GET /api/deliveries/{id}` - Get delivery by ID
- `GET /api/deliveries/{id}?includeTracking=true` - Get delivery with tracking (triggers external API call)
- `POST /api/deliveries/{orderId}` - Create a new delivery
- `PATCH /api/deliveries/{id}/status` - Update delivery status

#### Authentication

- `GET /api/auth/login` - Get login URL
- `GET /api/auth/login-success` - OAuth2 success callback
- `GET /api/auth/login-failure` - OAuth2 failure callback
- `GET /api/auth/user` - Get current authenticated user
- `POST /api/auth/logout` - Logout

---

## Testing Instructions

### Using Swagger UI

1. Access Swagger UI at `http://localhost:8080/swagger-ui.html`
2. If security is enabled, click "Authorize" and authenticate with Google OAuth
3. Test endpoints directly from the Swagger interface

### Using cURL Examples

#### Create an Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "items": [
      {
        "productName": "Product 1",
        "quantity": 2,
        "price": 99.99
      }
    ]
  }'
```

#### Create a Delivery

```bash
curl -X POST http://localhost:8080/api/deliveries/{orderId} \
  -H "Content-Type: application/json" \
  -d '{
    "street": "Main Street",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01001000"
  }'
```

#### Get Delivery with Tracking (triggers external API)

```bash
curl -X GET "http://localhost:8080/api/deliveries/{deliveryId}?includeTracking=true"
```

This endpoint:
1. Retrieves delivery data from PostgreSQL
2. Calls ViaCEP API to get address information (if not cached)
3. Stores address data in MongoDB
4. Retrieves tracking events from MongoDB
5. Combines all data in the response

### Authentication Testing

If `SECURITY_ENABLED=true`:

1. Access `/oauth2/authorization/google` to initiate OAuth flow
2. Authenticate with Google
3. Use the returned JWT token in subsequent requests:

```bash
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Security and Authentication

### OAuth2 Authentication

The application uses Spring Security with OAuth2 for authentication. Currently configured to work with:

- **Google OAuth2** (primary)
- **Auth0** (optional, via JWT resource server)

### Configuration

OAuth2 is configured in `application.yaml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            redirect-uri: ${GOOGLE_REDIRECT_URI}
            scope:
              - profile
              - email
```

### Setting up Google OAuth2

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API
4. Go to "Credentials" → "Create Credentials" → "OAuth 2.0 Client ID"
5. Configure:
   - Application type: Web application
   - Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
   - Authorized JavaScript origins: `http://localhost:8080`
6. Copy Client ID and Client Secret to `.env` file

### Security Configuration

Security can be enabled/disabled via environment variable:

- `SECURITY_ENABLED=false` - All endpoints are public (development)
- `SECURITY_ENABLED=true` - API endpoints require authentication (production)

---

## External API Integration

### ViaCEP Integration

The application integrates with [ViaCEP API](https://viacep.com.br/) to retrieve address information based on Brazilian ZIP codes.

**Implementation:**
- Uses Spring WebClient for HTTP requests
- Caches address data in MongoDB to avoid redundant API calls
- Handles errors gracefully (returns null if API is unavailable)

**Business Logic:**
- When `GET /api/deliveries/{id}?includeTracking=true` is called:
  1. Retrieves delivery from PostgreSQL
  2. Checks MongoDB for cached address info
  3. If not cached, calls ViaCEP API
  4. Stores address info in MongoDB
  5. Combines PostgreSQL + MongoDB data in response

---

## Deployment

### Docker Support

The application includes a `Dockerfile` for containerization.

#### Build Docker Image

```bash
docker build -t delivery-api .
```

#### Run Docker Container

```bash
docker run -p 8080:8080 \
  -e POSTGRES_URL=jdbc:postgresql://host.docker.internal:5432/delivery \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e MONGO_URI=mongodb://host.docker.internal:27017/delivery \
  delivery-api
```

#### Docker Compose

The `docker-compose.yml` file includes all services:

```bash
# Start all services
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Production Deployment

For production deployment:

1. Set `SECURITY_ENABLED=true`
2. Configure production database URLs
3. Update OAuth redirect URIs to production domain
4. Configure CORS allowed origins
5. Use environment variables for all sensitive data
6. Enable HTTPS

---

## Known Issues

1. **External API Dependency**: The application depends on ViaCEP API availability. If the external service is down, address information will not be retrieved.

2. **OAuth Token Management**: Token generation is delegated to the OAuth provider (Google/Auth0). The backend acts as a resource server and does not handle token generation internally.

3. **Session Management**: OAuth2 login sessions are managed by Spring Security. For stateless API usage, consider implementing JWT token validation.

4. **MongoDB Connection**: If MongoDB is unavailable, the application will fail to start. Consider implementing connection retry logic or making MongoDB optional for basic CRUD operations.

---

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security OAuth2](https://spring.io/projects/spring-security-oauth)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Swagger/OpenAPI](https://swagger.io/)
- [ViaCEP API](https://viacep.com.br/)

---

## License

This project is licensed under the Apache 2.0 License.

---

## Author

Developed as part of the ZCAM technical assessment for Java Backend Developer position.
