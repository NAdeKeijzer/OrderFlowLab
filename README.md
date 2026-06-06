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
* Handle asynchronous inventory reservation
* Confirm orders after successful inventory reservation
* Mark orders as failed when inventory reservation fails

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

* Create inventory items
* Retrieve inventory items
* Reserve stock when orders are created
* Prevent reservation when inventory is missing
* Prevent reservation when stock is insufficient
* Reduce available stock after reservation
* Inventory updates are transactional

---

# 🧱 Project Structure

```text
src/main/kotlin/org/nikita/orderflowlab
│   OrderFlowLabApplication.kt
│
├── common
│   └── ApiExceptionHandler.kt
│
├── config
│   ├── JacksonConfig.kt
│   ├── KafkaConsumerConfig.kt
│   └── KafkaProducerConfig.kt
│
├── inventory
│   ├── api
│   ├── dto
│   ├── exception
│   ├── model
│   ├── repository
│   └── service
│
└── order
    ├── api
    ├── dto
    ├── event
    ├── exception
    ├── model
    ├── repository
    └── service
```

The project is organized by domain (`order`, `inventory`) and layered by responsibility (`api`, `service`, `repository`, `model`, etc.).

Tests:

```text
src/test/kotlin/org/nikita/orderflowlab
│   OrderFlowLabApplicationTests.kt
│
├── inventory
    ├── event
    └── service
│
└── order    │
    ├── api
    ├── event
    ├── model
    ├── repository
    └── service
```

Database migrations:

```text
src/main/resources/db/migration
├── V1__create_order_tables.sql
├── V2__add_unit_price_to_order_lines.sql
├── V3__create_inventory_reservations.sql
└── V4__create_inventory_items.sql
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

# 📦 Inventory API

## Create or Update Inventory Item

Creates a new inventory item, or updates the available quantity when the product already exists.

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/inventory-items" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{
    "productId": "22222222-2222-2222-2222-222222222222",
    "availableQuantity": 10
  }'
```

---

## Get Inventory Item

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/inventory-items/22222222-2222-2222-2222-222222222222"
```

---

# 📦 Order API

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

Cancels an order. When the order has successfully reserved inventory, cancelling the order also releases the inventory reservation and increases the available stock again.

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/orders/{id}/cancel" `
  -Method Patch
```

---



## Inventory Reservation Flow

When an order is created, inventory is reserved asynchronously through Kafka.

Example flow:

1. Create an inventory item with quantity `10`
2. Create an order for quantity `2`
3. `OrderCreatedEvent` is published to Kafka
4. `OrderCreatedConsumer` receives the event
5. `OrderWorkflowService` starts the inventory reservation workflow
6. `InventoryReservationService` reserves inventory
7. Available quantity is reduced from `10` to `8`
8. `InventoryReservedEvent` is published
9. `InventoryReservedConsumer` receives the event
10. Order status transitions:

```text
CREATED
→ INVENTORY_RESERVED
→ CONFIRMED
```

If inventory is missing or insufficient:

```text
CREATED
→ INVENTORY_FAILED
```

Inventory reservation is transactional:

- reservations are rolled back on failure
- inventory quantities remain unchanged

Cancellation flow:

```text
CONFIRMED → CANCELLED
```

When a confirmed order is canceled:
- inventory reservations for the order are released
- available inventory quantities are increased again
- reservation records are removed

---

## Manual Flow Checks

### Successful reservation and cancellation

1. Create inventory items for all products in the order.
2. Create an order.
3. Retrieve the order and verify the status is `CONFIRMED`.
4. Retrieve the inventory items and verify quantities are reduced.
5. Cancel the order.
6. Retrieve the order and verify the status is `CANCELLED`.
7. Retrieve the inventory items again and verify quantities are restored.

### Failed reservation

1. Create an inventory item with insufficient quantity, or do not create inventory for one of the products.
2. Create an order.
3. Retrieve the order and verify the status is `INVENTORY_FAILED`.
4. Verify inventory quantities remain unchanged.

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

## 🧠 What This Project Demonstrates

* Layered architecture
* DTO validation
* REST API design
* Domain modeling
* Event-driven architecture
* Event-driven workflow orchestration
* Domain events between bounded contexts
* Kafka producers & consumers
* Saga-style process flow
* Flyway database migrations
* Integration testing with MockMvc
* Kotlin + Spring Boot development
* Asynchronous inventory reservation flow
* Event consumers with persistence

---

# 🔮 Possible Improvements

* Payment service integration
* InventoryReservationFailedEvent
* Optimistic locking for inventory concurrency
* Retry policies & dead-letter queues
* Outbox pattern
* Distributed tracing
* OpenAPI / Swagger
* Authentication & authorization
* Testcontainers
* CI/CD pipeline
* Kubernetes deployment
* AWS deployment

---

# 👩‍💻 Author

Nikita de Keijzer

Backend developer exploring Kotlin, Spring Boot, clean architecture, and event-driven systems.
