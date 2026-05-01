package org.nikita.orderflowlab.order

import jakarta.validation.Valid
import org.nikita.orderflowlab.order.dto.CreateOrderRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(@Valid @RequestBody request: CreateOrderRequest):
            ResponseEntity<Order> {
        val order = orderService.createOrder(request.customerId!!)
        return ResponseEntity.ok(order)
    }

    @GetMapping("/{id}")
    fun getOrder(@PathVariable id: UUID): ResponseEntity<Order> {
        val order = orderService.getOrder(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(order)
    }

    @GetMapping
    fun getAll(): List<Order> = orderService.getAll()
}