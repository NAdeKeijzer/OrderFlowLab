package org.nikita.orderflowlab.order

import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    fun createOrder(customerId: UUID): Order {
        val order = Order(customerId = customerId)
        return orderRepository.save(order)
    }

    fun getOrder(id: UUID): Order? {
        return orderRepository.findById(id).orElse(null)
    }

    fun getAll(): List<Order> {
        return orderRepository.findAll()
    }
}