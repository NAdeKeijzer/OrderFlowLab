# OrderFlowLab

A Spring Boot + Kotlin project to explore order management flows, including validation, pricing, state transitions, and API design.

## 🚀 Tech Stack

* Kotlin
* Spring Boot 4
* Spring Web (REST APIs)
* Spring Data JPA
* Flyway (database migrations)
* PostgreSQL
* H2 (local/test database)
* JUnit 5 + MockMvc (testing)

---

## 📦 Features

* Create orders
* Add order lines (products + quantity + unit price)
* Calculate line totals and order totals
* Retrieve orders
* Validate input (e.g. missing customerId, invalid quantities)
* Order status lifecycle:
    * CREATED
    * PAID
    * CANCELLED
* Pay orders
* Cancel orders

---

## 🧱 Project Structure
src/main/kotlin/org/nikita/orderflowlab
├── order
│   ├── Order.kt
│   ├── OrderLine.kt
│   ├── OrderController.kt
│   ├── OrderService.kt
│   ├── OrderRepository.kt
│   ├── OrderStatus.kt
│   ├── OrderLineInput.kt
│   ├── dto
│   │   ├── CreateOrderRequest.kt
│   │   ├── CreateOrderLineRequest.kt
│   │   ├── OrderResponse.kt
│   │   └── OrderLineResponse.kt
│   └── exception
└── common

src/test/kotlin/org/nikita/orderflowlab
└── order
├── OrderControllerTest.kt
├── OrderServiceTest.kt
├── OrderRepositoryTest.kt
├── OrderTest.kt
└── OrderLineTest.kt

# OrderFlowLab

A Spring Boot + Kotlin project to explore order management flows, including validation, pricing, state transitions, and API design.

---