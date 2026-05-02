package org.nikita.orderflowlab.order

import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    fun createOrder(
        customerId: UUID,
        items: List<CreateOrderLineRequest>
    ): Order {
        val order = Order(customerId = customerId)

        items.forEach {
            order.addLine(
                productId = it.productId!!,
                quantity = it.quantity
            )
        }

        return orderRepository.save(order)
    }

    fun getOrder(id: UUID): Order? {
        return orderRepository.findById(id).orElse(null)
    }

    fun getAll(): List<Order> {
        return orderRepository.findAll()
    }

    fun markAsPaid(id: UUID): Order {
        val order = orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found") }

        order.status = OrderStatus.PAID
        return orderRepository.save(order)
    }
}