# CraveDash – Multi-Restaurant Food Ordering & Delivery Orchestration Engine

## Project Overview

CraveDash is a microservices-based food ordering and delivery backend designed to support multiple restaurants, menu management, order processing, restaurant availability checks, payment processing, service discovery, API routing, load balancing, and JWT-based authentication.

The project is developed as part of **SOA Programming and Microservices (24SDCS03R)**.

## Architecture

```
React Frontend
      |
      v
API Gateway :8080
      |
      +-------------------+
      |         |         |
      v         v         v
 Order      Restaurant   Payment
Service     Service      Service
 :8082       :8081       :8083
      |         ^
      |         |
      +---------+
        REST communication

        Eureka Server
           :8761
             |
   Service Discovery & Load Balancing
```

### Services

| Service | Port | Responsibility |
|---|---:|---|
| Eureka Server | 8761 | Service discovery |
| API Gateway | 8080 | Central API entry point and JWT security |
| Restaurant Service | 8081 / 8091 | Restaurants, menus and availability |
| Order Service | 8082 / 8092 | Order creation and order management |
| Payment Service | 8083 / 8093 | Payment processing and payment records |

Multiple instances are used to demonstrate service discovery and load balancing.

## Main Technologies

- Java 25
- Spring Boot 4.1.1
- Spring Cloud 2025.1.x
- Spring Web MVC
- Spring Data JPA
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway Server Web MVC
- Spring Cloud LoadBalancer
- Spring Security
- JWT
- MySQL
- Maven
- REST APIs
- React (frontend)

## Service Communication

The main order flow is:

1. Client authenticates through the API Gateway.
2. Client sends an order request through the API Gateway.
3. Order Service checks restaurant availability through Restaurant Service.
4. Order Service creates and stores the order.
5. Order Service sends the payment request to Payment Service.
6. A successful payment changes the order status to `CONFIRMED`.
7. The response is returned through the API Gateway.

## JWT Authentication

The API Gateway provides JWT-based authentication.

Login endpoint:

```
POST /auth/login
```

Example request:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

The successful login returns a Bearer JWT token. Protected APIs require:

```
Authorization: Bearer <token>
```

## API Gateway Routes

| Path | Destination |
|---|---|
| `/orders/**` | ORDER-SERVICE |
| `/restaurants/**` | RESTAURANT-SERVICE |
| `/payments/**` | PAYMENT-SERVICE |
| `/auth/**` | Authentication |

## Git Branch Structure

The team repository is divided into three development branches:

- **teammate-1** – Restaurant Service
- **teammate-2** – Order Service and Payment Service
- **teammate-3** – Eureka Server and API Gateway/JWT

The **main** branch contains the complete integrated project.

## Project Structure

```
cravedash-team/
├── api-gateway/
├── eureka-server/
├── order-service/
├── payment-service/
├── restaurant-service/
└── README.md
```

## Database

Each business service uses its own MySQL database:

- `cravedash_restaurant_db`
- `cravedash_order_db`
- `cravedash_payment_db`

Database credentials are configured locally in each service's `application.properties`.

## Running the Project

Start the services in this general order:

1. Eureka Server
2. Restaurant Service
3. Payment Service
4. Order Service
5. API Gateway
6. React Frontend

Then open the Eureka dashboard:

```
http://localhost:8761
```

The API Gateway is available at:

```
http://localhost:8080
```

## Team Development

Each teammate works on their assigned branch. Completed work can later be merged into `main` after testing and integration.

Before pushing changes:

```
git pull
git add .
git commit -m "Describe your changes"
git push
```

## Project Goals

- Implement a real microservices architecture.
- Demonstrate Eureka service discovery.
- Demonstrate API Gateway routing.
- Implement JWT authentication.
- Demonstrate load balancing with multiple service instances.
- Implement REST-based inter-service communication.
- Provide independent databases for business services.
- Build a complete food ordering workflow.
- Integrate the backend with a React frontend.
