package org.nikita.orderflowlab.order

import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    @Transactional
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

    @Transactional(readOnly = true)
    fun getOrder(id: UUID): Order? {
        return orderRepository.findById(id).orElse(null)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<Order> {
        return orderRepository.findAll()
    }

    @Transactional
    fun markAsPaid(id: UUID): Order {
        val order = orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found") }

        if (order.status == OrderStatus.PAID) {
            throw OrderAlreadyPaidException(id)
        }

        order.status = OrderStatus.PAID

        return orderRepository.save(order)
    }

    @Transactional
    fun cancelOrder(id: UUID): Order {
        val order = orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found") }

        order.status = OrderStatus.CANCELLED

        return orderRepository.save(order)
    }
}