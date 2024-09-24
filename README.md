# Currency Gateway

## Project Overview

The **Currency Gateway** is a Spring Boot-based microservice designed to fetch and serve currency data from an external API, manage requests with Redis, and log service activity with RabbitMQ. The application fetches the latest currency exchange rates from the Fixer.io API and stores them in a PostgreSQL database. Users can query the current and historical currency data through JSON and XML APIs.

### Features:
- Fetches currency data from Fixer.io API at regular intervals.
- Stores currency data in PostgreSQL.
- Caches request IDs using Redis to prevent duplicate requests.
- Logs requests in PostgreSQL and RabbitMQ.
- Provides endpoints for fetching current and historical currency data.

## System Architecture

- **Spring Boot**: The main framework used for building the application.
- **PostgreSQL**: Stores currency data and request logs.
- **Redis**: Used for caching and duplicate request checking.
- **RabbitMQ**: Used for logging requests.

### Technologies and Libraries:
- Java 17
- Spring Boot 3.3.3
- Hibernate ORM
- PostgreSQL
- Redis
- RabbitMQ
- Fixer.io API for currency exchange rates

## Installation and Setup

### Prerequisites:
- Docker and Docker Compose installed on your machine
- Java 17 installed
- Maven installed

### Environment Configuration:
The project relies on environment variables. The `.env` file contains configurations for PostgreSQL, Redis, RabbitMQ, and the Fixer.io API. Ensure the following environment variables are set in your `.env` file:

```bash
# Database Configuration
DB_URL=jdbc:postgresql://postgres:5432/currency_gateway
DB_USERNAME=admin
DB_PASSWORD=admin

# Redis Configuration
REDIS_HOST=redis
REDIS_PORT=6379

# RabbitMQ Configuration
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672

# Fixer.io API Configuration
FIXER_API_URL=https://data.fixer.io/api/latest
FIXER_API_KEY=your_fixer_api_key
## Docker Setup:

The project uses Docker Compose to run PostgreSQL, Redis, RabbitMQ, and the application itself.

Run the following command to start the services:

```bash
docker-compose up --build
```

## API Endpoints:

### JSON API

#### Get Current Currency Data
- **Endpoint**: `/json_api/current`
- **Method**: `POST`
- **Request body**:

```json
{
  "requestId": "123",
  "currency": "EUR"
}
```

- **Response**: Current exchange rates for the specified currency.

#### Get Currency History
- **Endpoint**: `/json_api/history`
- **Method**: `POST`
- **Request body**:

```json
{
  "requestId": "124",
  "currency": "USD",
  "period": 24
}
```

- **Response**: Historical exchange rates for the specified currency within the given period (in hours).

## Testing

### Integration and Unit Tests

Tests can be run using Maven:

```bash
./mvnw test
```

Integration tests are configured in the `src/test/java` directory and cover the APIs and services. Unit tests ensure the correctness of individual components.

## Troubleshooting

- **Redis Connection**: If Redis is not connecting, ensure Redis is up and running (`docker-compose logs redis`) and that the environment variables for `REDIS_HOST` and `REDIS_PORT` in both `.env` and `docker-compose.yml` files are correctly set to `redis` and `6379` respectively.
- **Fixer.io API**: Ensure that you have provided a valid API key for Fixer.io in your `.env` file.
