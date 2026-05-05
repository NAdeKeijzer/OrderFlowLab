# OrderFlowLab

A Spring Boot + Kotlin project to explore order management flows, including validation, state transitions, and API design.

## 🚀 Tech Stack

* Kotlin
* Spring Boot 4
* Spring Web (REST APIs)
* Spring Data JPA
* Flyway (database migrations)
* H2 (local/test database)
* JUnit 5 + MockMvc (testing)

---

## 📦 Features

* Create orders
* Add order lines (products + quantity)
* Retrieve orders
* Validate input (e.g. missing customerId, invalid quantities)
* Order status lifecycle (CREATED → PAID)

---

## 🧱 Project Structure

```
src/main/kotlin/org/nikita/orderflowlab
├── order
│   ├── Order.kt
│   ├── OrderController.kt
│   ├── OrderService.kt
│   ├── OrderRepository.kt
│   └── dto
│       ├── CreateOrderRequest.kt
│       └── CreateOrderLineRequest.kt
└── common
```

Tests:

```
src/test/kotlin/org/nikita/orderflowlab
└── order
    ├── OrderControllerTest.kt
    └── OrderServiceTest.kt
```

---

## ⚙️ Running the application

### Option 1 — Default (H2 in-memory database)

Start the app:

```
./gradlew bootRun
```

App will run on:

```
http://localhost:8080
```

This uses an in-memory H2 database. Data is lost when the app stops.

---

### Option 2 — PostgreSQL (recommended)

#### 1. Start PostgreSQL via Docker

```
docker compose -f infrastructure/docker-compose.yml up -d
```

#### 2. Run the application with the postgres profile

```
./gradlew bootRun --args='--spring.profiles.active=postgres'
```

---

### Verify the application is running

```
http://localhost:8080/actuator/health
```

---

## 🧪 Running tests

```
./gradlew clean test
```

---

## 🔍 Example API usage

### Create order

```bash
Invoke-RestMethod \
  -Uri "http://localhost:8080/orders" \
  -Method Post \
  -ContentType "application/json" \
  -Body '{
    "customerId": "7a6d3f6e-91d4-4a29-a8a7-2e69e5e5d4a1",
    "items": [
      {
        "productId": "9aaf8f51-0c4f-4e12-9887-9e10c32fbe91",
        "quantity": 2
      }
    ]
  }'
```

---

### Get all orders

```bash
Invoke-RestMethod http://localhost:8080/orders
```

---

### Get order by ID

```bash
Invoke-RestMethod http://localhost:8080/orders/{id}
```

---

## 🧪 Validation examples

The API validates:

* `customerId` must not be null
* `items` must not be empty
* `quantity` must be > 0

Invalid input returns:

```
400 Bad Request
```

---

## 🧠 What this project demonstrates

* Clean layered architecture (Controller → Service → Repository)
* DTO-based validation
* Test-driven development (service + controller tests)
* REST API design
* Domain modeling (Order + OrderLine + Status)

---

## 🔮 Possible improvements

* Add order payment flow (`/orders/{id}/pay`)
* Add cancellation flow
* Introduce domain events
* Add integration with external payment service
* Replace H2 with PostgreSQL for production profile
* Add Docker support

---

## 👩‍💻 Author

Nikita de Keijzer
Java/Kotlin backend developer exploring clean architecture and domain modeling
