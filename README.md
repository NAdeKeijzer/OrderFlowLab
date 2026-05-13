# OrderFlowLab

A Kotlin + Spring Boot project to explore order management flows, validation, REST APIs, database migrations, and event-driven architecture using Kafka.

---

## 🚀 Tech Stack

* Kotlin
* Java 21
* Spring Boot 4
* Spring Web MVC
* Spring Data JPA
* PostgreSQL
* H2 Database
* Flyway
* Apache Kafka
* Docker Compose
* JUnit 5
* MockMvc

---

## 📦 Features

### Orders

* Create orders
* Retrieve all orders
* Retrieve order by id
* Pay orders
* Cancel orders

### Validation

* `customerId` is required
* orders must contain at least one item
* quantity must be greater than zero
* unitPrice must be positive

### Pricing

* Line totals are calculated automatically
* Order total price is calculated automatically

### Persistence

* PostgreSQL profile for local development
* H2 in-memory database for tests
* Flyway database migrations

### Kafka

* Publish `OrderCreatedEvent`
* Consume `OrderCreatedEvent`
* JSON serialization/deserialization
* Kafka integration via Docker

### Inventory

* Reserve inventory for created orders
* Persist inventory reservations
* Consume order events asynchronously

---

# 🧱 Project Structure

```text
src/main/kotlin/org/nikita/orderflowlab
```text
src/main/kotlin/org/nikita/orderflowlab
├── config
│   ├── JacksonConfig.kt
│   ├── KafkaConsumerConfig.kt
│   └── KafkaProducerConfig.kt
│
├── inventory
│   ├── InventoryReservation.kt
│   ├── InventoryReservationRepository.kt
│   └── InventoryReservationService.kt
│
├── order
│   ├── Order.kt
│   ├── OrderController.kt
│   ├── OrderRepository.kt
│   ├── OrderService.kt
│   ├── OrderStatus.kt
│   │
│   ├── dto
│   │   ├── CreateOrderRequest.kt
│   │   ├── CreateOrderLineRequest.kt
│   │   └── OrderResponse.kt
│   │
│   └── event
│       ├── KafkaOrderEventPublisher.kt
│       ├── NoOpOrderEventPublisher.kt
│       ├── OrderCreatedConsumer.kt
│       ├── OrderCreatedEvent.kt
│       ├── OrderCreatedEventHandler.kt
│       └── OrderEventPublisher.kt
│
└── OrderFlowLabApplication.kt
```
```

Tests:

```text
src/test/kotlin/org/nikita/orderflowlab
├── inventory
│   └── InventoryReservationServiceTest.kt
│
├── order
│   ├── OrderControllerTest.kt
│   ├── OrderServiceTest.kt
│   │
│   └── event
│       └── OrderCreatedEventHandlerTest.kt
│
└── OrderFlowLabApplicationTests.kt
```
```

Database migrations:

```text
src/main/resources/db/migration
├── V1__create_order_tables.sql
├── V2__add_unit_price_to_order_lines.sql
└── V3__create_inventory_reservations.sql
```
```

---

# ⚙️ Running the Application

## 1. Start infrastructure

Start PostgreSQL and Kafka:

```bash
docker compose -f infrastructure/docker-compose.yml up -d
```

Verify:

```bash
docker ps
```

You should see:

* `orderflowlab-postgres`
* `orderflowlab-kafka`

---

## 2. Run the application

```bash
./gradlew bootRun --args='--spring.profiles.active=postgres'
```

Application runs on:

```text
http://localhost:8080
```

---

# 🧪 Running Tests

```bash
./gradlew clean test
```

---

# 🔍 Example API Usage

## Create Order

### PowerShell

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/orders" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{
    "customerId": "11111111-1111-1111-1111-111111111111",
    "items": [
      {
        "productId": "22222222-2222-2222-2222-222222222222",
        "quantity": 2,
        "unitPrice": 9.99
      },
      {
        "productId": "33333333-3333-3333-3333-333333333333",
        "quantity": 1,
        "unitPrice": 5.50
      }
    ]
  }'
```

---

## Get All Orders

```powershell
Invoke-RestMethod http://localhost:8080/orders
```

---

## Get Order By Id

```powershell
Invoke-RestMethod http://localhost:8080/orders/{id}
```

---

## Pay Order

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/orders/{id}/pay" `
  -Method Patch
```

---

## Cancel Order

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/orders/{id}/cancel" `
  -Method Patch
```

---

# 📨 Kafka

When an order is created, the application publishes an event to Kafka topic:

```text
order.created
```

Example payload:

```json
{
  "orderId": "defbdef5-cbff-418e-8195-49a4f1fc732a",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "totalPrice": 25.48,
  "createdAt": "2026-05-13T12:42:29Z",
  "lines": [
    {
      "productId": "22222222-2222-2222-2222-222222222222",
      "quantity": 2
    }
  ]
}
```

The application also contains a Kafka consumer that listens to this topic.

---

# 🐘 PostgreSQL Profile

The `postgres` Spring profile enables:

* PostgreSQL datasource
* Kafka integration
* Flyway migrations

Configuration lives in:

```text
src/main/resources/application-postgres.yml
```

---

# 🧠 What This Project Demonstrates

* Layered architecture
* DTO validation
* REST API design
* Domain modeling
* Event-driven architecture
* Kafka producers & consumers
* Flyway database migrations
* Integration testing with MockMvc
* Kotlin + Spring Boot development
* Asynchronous inventory reservation flow
* Event consumers with persistence

---

# 🔮 Possible Improvements

* Payment service integration
* Dead-letter queue handling
* Kafka retries and error handling
* Outbox pattern
* OpenAPI / Swagger
* Authentication & authorization
* Testcontainers
* CI/CD pipeline

---

# 👩‍💻 Author

Nikita de Keijzer

Backend developer exploring Kotlin, Spring Boot, clean architecture, and event-driven systems.
