# Unstray API Gateway

A modern, cloud-native API Gateway built with Spring Cloud Gateway and Spring Boot, serving as the central entry point for the Unstray Cloud backend platform microservices ecosystem.

## 📋 Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Building](#building)
- [Running](#running)
- [Features](#features)
- [API Routes](#api-routes)
- [Monitoring & Health](#monitoring--health)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Unstray API Gateway is a critical component of the Unstray Cloud backend platform that:

- **Routes requests** to appropriate microservices based on URL patterns
- **Discovers services** dynamically using Netflix Eureka service registry
- **Centralizes configuration** using Spring Cloud Config Server
- **Monitors system health** with Spring Boot Actuator endpoints
- **Handles cross-cutting concerns** such as authentication, rate limiting, and request/response transformation

## Technology Stack

| Component            | Version         | Purpose                               |
| -------------------- | --------------- | ------------------------------------- |
| Java                 | 25              | Programming language                  |
| Spring Boot          | 4.1.0           | Application framework                 |
| Spring Cloud         | 2025.1.2        | Microservices support                 |
| Spring Cloud Gateway | Latest (WebMvc) | API routing and gateway functionality |
| Netflix Eureka       | Latest          | Service discovery and registration    |
| Spring Cloud Config  | Latest          | Centralized configuration management  |
| Spring Boot Actuator | Latest          | Monitoring and health checks          |

## Prerequisites

- **Java**: JDK 25 or higher
- **Maven**: 3.8.1 or higher
- **Config Server**: Running Spring Cloud Config Server (default: `http://localhost:8888`)
- **Eureka Server**: Running Netflix Eureka service registry (for service discovery)

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd unstray-api-gateway
```

### Install Dependencies

```bash
mvn clean install
```

## Configuration

### Application Configuration

The application is configured via `src/main/resources/application.yaml`:

```yaml
server:
  port: 8080 # Server port

spring:
  application:
    name: api-gateway # Application name for service registry
  config:
    import: "optional:configserver:http://localhost:8888" # Config server URL
```

### Service Routes

Uncomment and configure routes in `application.yaml` to define how requests are forwarded to downstream microservices:

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true

      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/v1/users/**

        - id: item-service
          uri: lb://item-service
          predicates:
            - Path=/api/v1/items/**

        - id: media-service
          uri: lb://media-service
          predicates:
            - Path=/api/v1/media/**
```

**Key Configuration Options:**

- `lb://` prefix enables load-balanced routing through Eureka
- `Path` predicates match incoming request paths
- Multiple predicates can be combined with AND logic
- Filters can be applied per route for transformation

### Environment Variables

Configure external services using environment variables:

```bash
# Config Server
CONFIG_SERVER_URL=http://config-server:8888

# Eureka Server
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
```

## Building

### Build the Application

```bash
mvn clean package
```

### Build with Tests

```bash
mvn clean package -DskipTests=false
```

### View Build Artifacts

Built artifacts are located in the `target/` directory:

- `api-gateway-0.0.1-SNAPSHOT.jar` - Executable JAR file
- `api-gateway-0.0.1-SNAPSHOT.jar.original` - Original JAR before repackaging

## Running

### Run Locally

```bash
mvn spring-boot:run
```

The gateway will start on `http://localhost:8080`

### Run the Built JAR

```bash
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

### Run with Custom Configuration

```bash
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar \
  --config.server.url=http://config-server:8888 \
  --eureka.client.serviceUrl.defaultZone=http://eureka-server:8761/eureka/
```

## Features

- ✅ **Dynamic Service Discovery** - Automatically discovers and routes to registered microservices
- ✅ **Centralized Configuration** - External configuration management via Spring Cloud Config
- ✅ **Load Balancing** - Built-in load balancing through Ribbon/Spring Cloud LoadBalancer
- ✅ **Health Monitoring** - Actuator endpoints for health checks and metrics
- ✅ **Flexible Routing** - Support for path-based, header-based, and method-based routing
- ✅ **Request/Response Transformation** - Filters for modifying requests and responses
- ✅ **Rate Limiting** - Optional rate limiting configuration (can be added via filters)

## API Routes

### Gateway Root Endpoints

| Endpoint            | Method | Description            |
| ------------------- | ------ | ---------------------- |
| `/actuator`         | GET    | Actuator base endpoint |
| `/actuator/health`  | GET    | Health status check    |
| `/actuator/metrics` | GET    | Metrics endpoint       |

### Downstream Service Routes

Once configured in `application.yaml`, the following routes become available:

- `/api/v1/users/**` → `user-service`
- `/api/v1/items/**` → `item-service`
- `/api/v1/media/**` → `media-service`

### Example Requests

```bash
# Health check
curl http://localhost:8080/actuator/health

# Get metrics
curl http://localhost:8080/actuator/metrics

# Route to user service
curl http://localhost:8080/api/v1/users

# Route to item service
curl http://localhost:8080/api/v1/items
```

## Monitoring & Health

### Health Endpoint

```bash
curl http://localhost:8080/actuator/health
```

**Response Example:**

```json
{
  "status": "UP",
  "components": {
    "discoveryClient": {
      "status": "UP"
    },
    "configServer": {
      "status": "UP"
    }
  }
}
```

### Available Metrics

```bash
curl http://localhost:8080/actuator/metrics
```

### Custom Monitoring

Enable additional actuator endpoints by adding to `application.yaml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,gateway,loggers
  endpoint:
    health:
      show-details: when-authorized
```

## Project Structure

```
unstray-api-gateway/
├── src/
│   ├── main/
│   │   ├── java/com/unstray/platform/api_gateway/
│   │   │   └── ApiGatewayApplication.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       └── java/com/unstray/platform/api_gateway/
│           └── ApiGatewayApplicationTests.java
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

## Contributing

1. **Create a feature branch**: `git checkout -b feature/your-feature`
2. **Make your changes**: Implement your feature or bug fix
3. **Write tests**: Ensure test coverage for your changes
4. **Commit your changes**: `git commit -m 'Add your feature'`
5. **Push to the branch**: `git push origin feature/your-feature`
6. **Create a Pull Request**: Submit your PR for review

### Code Style

- Follow Java conventions and Spring Boot best practices
- Use meaningful commit messages
- Ensure all tests pass before submitting a PR
- Add documentation for new features

## Troubleshooting

### Common Issues

**Q: Gateway won't start - "Failed to connect to Config Server"**

```
A: Ensure the Config Server is running at the configured URL.
   Set 'optional:' prefix in the config import to make it optional during development.
```

**Q: Service routes not working - "503 Service Unavailable"**

```
A: Verify that:
   1. The target service is registered with Eureka
   2. The service name in the route matches the Eureka registry
   3. The service is healthy and responding
```

**Q: Port 8080 already in use**

```
A: Change the server port in application.yaml or via command line:
   java -jar api-gateway-0.0.1-SNAPSHOT.jar --server.port=8081
```

## References

- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Netflix Eureka Documentation](https://github.com/Netflix/eureka/wiki)
- [Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

## License

This project is part of the Unstray Cloud backend platform. License details are available in the LICENSE file.

---

**For questions or support, please open an issue in the repository or contact the development team.**
