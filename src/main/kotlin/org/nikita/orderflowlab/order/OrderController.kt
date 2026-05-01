package org.nikita.orderflowlab.order

import jakarta.validation.Valid
import org.nikita.orderflowlab.order.dto.CreateOrderRequest
import org.nikita.orderflowlab.order.dto.OrderResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(
        @Valid @RequestBody request: CreateOrderRequest
    ): ResponseEntity<OrderResponse> {
        val order = orderService.createOrder(
            customerId = request.customerId!!,
            items = request.items
        )

        return ResponseEntity.ok(OrderResponse.from(order))
    }
    @GetMapping("/{id}")
    fun getOrder(@PathVariable id: UUID): ResponseEntity<OrderResponse> {
        val order = orderService.getOrder(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(OrderResponse.from(order))
    }
    @GetMapping
    fun getAll(): List<OrderResponse> =
        orderService.getAll().map { OrderResponse.from(it) }
}