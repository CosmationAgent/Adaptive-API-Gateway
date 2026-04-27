# Adaptive API Gateway

A high-performance API gateway built with **Java Spring Boot 3**, designed for rate limiting, load control, and resilience patterns to maintain system stability under heavy traffic.

## Overview

This is a production-ready API gateway starter that demonstrates modern Spring Boot development practices, including request logging, health monitoring, and extensible rate limiting architecture.

## Features

- **Controllers**: Example `TestController` for API endpoint demonstration
- **Request Logging Filter**: Logs HTTP requests with method, URI, status code, and response time
- **Rate Limiter Service (Stub)**: Interface and placeholder for future rate limiting logic
- **Swagger/OpenAPI**: Enabled for API documentation and testing via Springdoc
- **Health Check Endpoint**: Available via Spring Boot Actuator at `/actuator/health`

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Web** (REST APIs)
- **Spring Boot Actuator** (Health checks)
- **Springdoc OpenAPI** (Swagger UI)
- **Lombok** (Reducing boilerplate)
- **Maven** (Build tool)

## Project Structure

```
src/
├── main/
│   ├── java/com/adaptive/gateway/
│   │   ├── AdaptiveApiGatewayApplication.java  # Main application entry point
│   │   ├── controller/
│   │   │   └── TestController.java              # Example REST controller
│   │   ├── filter/
│   │   │   └── RequestLoggingFilter.java        # Request logging filter
│   │   └── service/
│   │       ├── IRateLimiter.java                # Rate limiter interface
│   │       └── RateLimiter.java                 # Rate limiter implementation (stub)
│   └── resources/
│       ├── application.properties               # Main configuration
│       └── application-dev.properties           # Development profile configuration
└── test/
    └── java/                                    # Unit tests
```

## Prerequisites

- **Java 17** or later
- **Maven 3.6+** or use the included Maven wrapper

## Getting Started

### 1. Build the Project

```bash
./mvnw clean package
```

Or if you have Maven installed:

```bash
mvn clean package
```

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

Or:

```bash
java -jar target/adaptive-api-gateway-1.0.0.jar
```

### 3. Run with Development Profile

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/test` | GET | Test endpoint returning "Hello from TestController!" |
| `/actuator/health` | GET | Health check endpoint |
| `/actuator/info` | GET | Application information |
| `/swagger-ui.html` | GET | Swagger UI for API documentation |
| `/api-docs` | GET | OpenAPI JSON specification |

## Testing the API

### Using cURL

```bash
# Test endpoint
curl http://localhost:8080/api/test

# Health check
curl http://localhost:8080/actuator/health
```

### Using Swagger UI

Navigate to `http://localhost:8080/swagger-ui.html` in your browser.

## Configuration

Configuration is managed through `application.properties`:

- **Server port**: `server.port=8080`
- **Logging levels**: Configured per package
- **Actuator endpoints**: Health and info exposed

## Development

### Adding New Controllers

Create a new class in `src/main/java/com/adaptive/gateway/controller/`:

```java
@RestController
@RequestMapping("/api/example")
public class ExampleController {
    @GetMapping
    public ResponseEntity<String> example() {
        return ResponseEntity.ok("Example response");
    }
}
```

### Implementing Rate Limiting

Modify `RateLimiter.java` to add actual rate limiting logic using:
- In-memory maps with sliding windows
- Redis for distributed rate limiting
- Spring's built-in rate limiting features

## Next Steps

- Implement actual rate limiting logic in `RateLimiter`
- Add database integration (PostgreSQL, MongoDB)
- Add Redis for distributed caching and rate limiting
- Implement circuit breaker patterns
- Add authentication and authorization
- Add more controllers for production gateway features
- Write comprehensive unit and integration tests

## License

This project is for educational and portfolio purposes.
